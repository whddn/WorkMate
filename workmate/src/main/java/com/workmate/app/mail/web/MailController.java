package com.workmate.app.mail.web;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.workmate.app.mail.service.MailFolderVO;
import com.workmate.app.mail.service.MailService;
import com.workmate.app.mail.service.MailVO;
import com.workmate.app.security.service.LoginUserVO;

import jakarta.mail.MessagingException;

@Controller
public class MailController {
	private final MailService mailService;

    @Autowired
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    
    
 // 메일 단건 조회
    @GetMapping("mail/view")
    public String viewMail(@RequestParam("mailId") int mailId, Model model) {
        MailVO mail = mailService.findMailById(mailId);
        model.addAttribute("mail", mail);
        return "mail/view";  
    }
    
    //메일 쓰기
    @GetMapping("mail/compose") 
	public String compose() {
		return "mail/compose";
	}
    //메일 보내는 기능
    @PostMapping("/mail/send")
    public String sendMail(
            @AuthenticationPrincipal LoginUserVO loginUser,
            @RequestParam String recipients,
            @RequestParam(required = false) String ccList,
            @RequestParam String subject,
            @RequestParam String content) {
        try {
            // ✅ 로그인한 사용자의 정보 가져오기
            String senderName = loginUser.getUserVO().getUserName();  // 사원 이름
            String senderEmail = loginUser.getUserVO().getUserMail(); // 사원 이메일

            // ✅ 메일 전송
            mailService.sendEmail(senderName, senderEmail, recipients, subject, content);

            return "redirect:/mail/mailmain"; // 성공 시 받은 메일함으로 이동
        } catch (MessagingException e) {
            return "전송 실패: " + e.getMessage();
        }
    }
    //보낸 메일함 전쳊
    @GetMapping("/mail/sent")
    public String sentMailList(@RequestParam(defaultValue = "1") int page,
                               Model model,
                               @AuthenticationPrincipal LoginUserVO loginUser) {

        int userNo = loginUser.getUserVO().getUserNo();
        int pageSize = 10;
        int folderId = 1002; // 보낸메일함

        int totalCount = mailService.countMailsByFolder(userNo, folderId); // 총 메일 수
        int totalPage = (int) Math.ceil((double) totalCount / pageSize);

        int blockSize = 5;
        int currentBlock = (int) Math.ceil((double) page / blockSize);
        int startPage = (currentBlock - 1) * blockSize + 1;
        int endPage = Math.min(startPage + blockSize - 1, totalPage);

        int offset = (page - 1) * pageSize;

        // 🔄 페이징된 메일 가져오기
        List<MailVO> sentMails = mailService.findMailListPaging(userNo, folderId, offset, pageSize);
        List<MailFolderVO> myFolders = mailService.findMailFolderList(userNo); // 사이드바 폴더용

        model.addAttribute("sentMails", sentMails);
        model.addAttribute("myFolders", myFolders);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("folderId", folderId);

        return "mail/sent"; // 
    }
 // 보낸 메일 상세 
    @GetMapping("mail/sentview")
    public String viewSentMail(@RequestParam("mailId") int mailId, Model model) {
        MailVO mail = mailService.findSentMailById(mailId);
        model.addAttribute("mail", mail);
        return "mail/sentview";
    }
    
 // **외부 메일 수신 (IMAP) 추가**
    @GetMapping("/mail/mailmain")
    public String receivedMailLists(@RequestParam(defaultValue = "1") int page,
                                    Model model,
                                    @AuthenticationPrincipal LoginUserVO loginUser) {

        int userNo = loginUser.getUserVO().getUserNo();
        int pageSize = 10;
        int folderId = 1001; // 받은메일함 고정

        int totalCount = mailService.countMailsByFolder(userNo, folderId);
        int totalPage = (int) Math.ceil((double) totalCount / pageSize);

        int blockSize = 5;
        int currentBlock = (int) Math.ceil((double) page / blockSize);
        int startPage = (currentBlock - 1) * blockSize + 1;
        int endPage = Math.min(startPage + blockSize - 1, totalPage);

        int offset = (page - 1) * pageSize;

        List<MailVO> receivedMails = mailService.findMailListPaging(userNo, folderId, offset, pageSize);
        List<MailFolderVO> myFolders = mailService.findMailFolderList(userNo);

        model.addAttribute("receivedMails", receivedMails);
        model.addAttribute("myFolders", myFolders);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "mail/mailmain";
    }
 
    //삭제 기능
    @PostMapping("/mail/deleteSelected")
    public String deleteSelectedMails(@RequestParam("mailIds") List<Integer> mailIds) {
        mailService.moveMailsToTrash(mailIds); // 서비스 호출
        return "redirect:/mail/mailmain"; // 받은메일함으로 리다이렉트
    }
    //휴지통
    @GetMapping("/mail/trash")
    public String trashPage(Model model, @AuthenticationPrincipal LoginUserVO loginUser) {
        int userNo = loginUser.getUserVO().getUserNo();
        List<MailVO> trashMails = mailService.findTrashMails(userNo); // 폴더 ID 1006번
        List<MailFolderVO> myFolders = mailService.findMailFolderList(userNo); // 사이드바 폴더 출력용
        model.addAttribute("trashMails", trashMails); // 반드시 있어야 함
        model.addAttribute("myFolders", myFolders); // 사이드바에서 사용
        return "mail/trash";
    }
    //메일 완전삭제
    @PostMapping("/mail/permanentDelete")
    public String dropMail(@RequestParam("mailIds") String mailIds) {
        List<Integer> mailIdList = Arrays.stream(mailIds.split(","))
                                         .map(Integer::parseInt)
                                         .toList();

        mailService.dropMail(mailIdList);
        return "redirect:/mail/trash";
    }

    // 폴더 추가
    @PostMapping("/mail/folder/add")
    public String inputMailFolder(@AuthenticationPrincipal LoginUserVO loginUser,
                                  @RequestParam String folderName) {
        int userNo = loginUser.getUserVO().getUserNo();

        MailFolderVO folder = new MailFolderVO();
        folder.setUserNo(userNo);
        folder.setFolderName(folderName);
        folder.setFolderType("TAG");
        folder.setEditable("Y");

        mailService.inputMailFolder(folder);
        return "redirect:/mail/mailmain";
    }
    
 // 폴더 삭제
    @PostMapping("/mail/folder/delete")
    public String dropMailFolder(@RequestParam int folderId,
                                 @AuthenticationPrincipal LoginUserVO loginUser) {
        int userNo = loginUser.getUserVO().getUserNo();

        mailService.dropMailFolder(folderId, userNo); // 유저 인증도 같이 확인
        return "redirect:/mail/mailmain";
    }
    
 // 특정 폴더 안에 있는 메일 리스트 조회
    @GetMapping("/mail/folder/{folderId}")
    public String viewFolderMails(@PathVariable int folderId,
                                  @RequestParam(defaultValue = "1") int page,
                                  @AuthenticationPrincipal LoginUserVO loginUser,
                                  Model model) {
        int userNo = loginUser.getUserVO().getUserNo();
        int pageSize = 10;
        int totalCount = mailService.countMailsByFolder(userNo, folderId);
        int totalPage = (int) Math.ceil((double) totalCount / pageSize);

        int blockSize = 5;
        int currentBlock = (int) Math.ceil((double) page / blockSize);
        int startPage = (currentBlock - 1) * blockSize + 1;
        int endPage = Math.min(startPage + blockSize - 1, totalPage);

        int offset = (page - 1) * pageSize;

        List<MailVO> mails = mailService.findMailListPaging(userNo, folderId, offset, pageSize);
        List<MailFolderVO> myFolders = mailService.findMailFolderList(userNo);
        MailFolderVO folderInfo = mailService.findMailFolderById(folderId);

        model.addAttribute("mails", mails);
        model.addAttribute("myFolders", myFolders);
        model.addAttribute("folderName", folderInfo.getFolderName());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("folderId", folderId);

        return "mail/folder";
    }
    
 // 선택한 메일을 다른 폴더로 이동
    @PostMapping("/mail/moveSelected")
    public String moveSelectedMails(@RequestParam("mailIds") String mailIds,
                                    @RequestParam("targetFolderId") int targetFolderId) {
        List<Integer> mailIdList = Arrays.stream(mailIds.split(","))
                                         .map(Integer::parseInt)
                                         .toList();

        mailService.modifyMailFolder(mailIdList, targetFolderId); // 서비스 호출

        return "redirect:/mail/mailmain";
    }
    
    //임시보관함
    @PostMapping("/mail/saveDraft")
    @ResponseBody
    public ResponseEntity<String> saveDraft(@RequestBody MailVO draftMail,
                                            @AuthenticationPrincipal LoginUserVO loginUser) {
        int userNo = loginUser.getUserVO().getUserNo();

        draftMail.setUserNo(userNo);
        draftMail.setFolderId(1003); // 임시보관함
        draftMail.setStatus("임시저장");
        draftMail.setEncrypted("N");
        draftMail.setIsSpam("N");
        draftMail.setMailType("임시");
        draftMail.setSentAt(new Date());

        mailService.inputMail(draftMail);
        return ResponseEntity.ok("Draft saved");
    }
    @GetMapping("/mail/draft")
    public String draftPage(Model model, @AuthenticationPrincipal LoginUserVO loginUser) {
        int userNo = loginUser.getUserVO().getUserNo();
        int folderId = 1003;

        List<MailVO> drafts = mailService.findMailListByFolderId(userNo, folderId);
        List<MailFolderVO> myFolders = mailService.findMailFolderList(userNo);

        model.addAttribute("draftMails", drafts); // ✅ draftMails 정확히 전달
        model.addAttribute("myFolders", myFolders);
        model.addAttribute("folderName", "임시보관함");

        return "mail/draft"; // ✅ 파일명도 정확
    }
}