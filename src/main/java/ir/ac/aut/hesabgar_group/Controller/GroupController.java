package ir.ac.aut.hesabgar_group.Controller;

import ir.ac.aut.hesabgar_group.domain.document.GroupInfo;
import ir.ac.aut.hesabgar_group.domain.repo.GroupInfoRepo;
import ir.ac.aut.hesabgar_group.manager.GroupManager;
import ir.ac.aut.hesabgar_group.request.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/group")
@CrossOrigin
public class GroupController {
    @Autowired
    private GroupManager groupManager;
    @Autowired
    private GroupInfoRepo groupInfoRepo;


    @PostMapping("/createGroup")
    public ResponseEntity<Object> createGroup(@RequestBody CreatingGroupRequest creatingGroupInfoRequest) {
        GroupInfo groupInfo = groupManager.createGroup(creatingGroupInfoRequest);
        if(groupInfo != null){
            return ResponseEntity.status(HttpStatus.OK).body(groupInfo);
        } return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @GetMapping("/getGroup/{groupId}")
    public ResponseEntity<Object> getGroup(@PathVariable String groupId){
        GroupInfo groupInfo = groupInfoRepo.getGroupInfoById(groupId);
        if(groupInfo != null){
            return ResponseEntity.status(HttpStatus.OK).body(groupInfo);
        } return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @PostMapping("/inviteToGroup")
    public ResponseEntity<Object> inviteGroup(@RequestBody InvitingToGroupRequest invitingToGroupInfoRequest) {
        GroupInfo groupInfo = groupManager.inviteToGroup(invitingToGroupInfoRequest);
        if(groupInfo != null){
            return ResponseEntity.status(HttpStatus.OK).body(groupInfo);
        } return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @PostMapping("/deleteGroup")
    public ResponseEntity<Object> deleteGroup(@RequestBody DeletingGroupRequest deletingGroupRequest) {
        String groupInfo = groupManager.deletingGroup(deletingGroupRequest);
        if(!groupInfo.equals("\"SUCCESSFULLY DELETED\"")){
            return ResponseEntity.status(HttpStatus.OK).body(groupInfo);
        } return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @PostMapping("/makingNewInvoiceAdmin")
    public ResponseEntity<Object> makingMemberInvoiceAdmin(@RequestBody MakingMemberInvoiceAdminRequest makingMemberInvoiceAdminRequest) {
        GroupInfo groupInfo = groupManager.makingMemberInvoiceAdmin(makingMemberInvoiceAdminRequest, true);
        if(groupInfo != null){
            return ResponseEntity.status(HttpStatus.OK).body(groupInfo);
        } return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @PostMapping("/dismissalInvoiceAdmin")
    public ResponseEntity<Object> DismissalMemberFromInvoiceAdmin(@RequestBody MakingMemberInvoiceAdminRequest dismissalMemberInvoiceAdminRequest) {
        GroupInfo groupInfo = groupManager.makingMemberInvoiceAdmin(dismissalMemberInvoiceAdminRequest, false);
        if(groupInfo != null){
            return ResponseEntity.status(HttpStatus.OK).body(groupInfo);
        } return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @DeleteMapping("/deleteAll")
    public void deleteAll() {
        groupInfoRepo.deleteAll();
    }

    @PostMapping("/deletingGroupMember")
    public ResponseEntity<Object> deletingGroupMember(@RequestBody DeletingGroupMemberRequest deletingGroupMemberRequest) {
        GroupInfo groupInfo = groupManager.deletingGroupMember(deletingGroupMemberRequest);
        if(groupInfo != null){
            return ResponseEntity.status(HttpStatus.OK).body(groupInfo);
        } return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @PostMapping("/addInvoice")
    public ResponseEntity<Object> getPaymentTerm(@RequestBody AddingInvoiceRequest addingInvoiceRequest) {
        GroupInfo groupInfo = groupManager.addingInvoice(addingInvoiceRequest);
        if(groupInfo != null){
            return ResponseEntity.status(HttpStatus.OK).body(groupInfo);
        } return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @PostMapping("/payInvoice")
    public ResponseEntity<Object> payInvoice(@RequestBody PayingInvoiceRequest payingInvoiceRequest) {
        GroupInfo groupInfo = groupManager.addingInvoice(payingInvoiceRequest);
        if(groupInfo != null){
            return ResponseEntity.status(HttpStatus.OK).body(groupInfo);
        } return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }


}
