package ir.ac.aut.hesabgar_group.helper;

import ir.ac.aut.hesabgar_group.domain.data.GroupMember;
import ir.ac.aut.hesabgar_group.domain.data.PaymentTerm;
import ir.ac.aut.hesabgar_group.domain.data.events.AddingInvoiceEvent;
import ir.ac.aut.hesabgar_group.domain.data.events.PayingInvoiceEvent;
import ir.ac.aut.hesabgar_group.domain.document.GroupInfo;
import ir.ac.aut.hesabgar_group.domain.document.UserInfo;
import ir.ac.aut.hesabgar_group.domain.repo.GroupInfoRepo;
import ir.ac.aut.hesabgar_group.domain.repo.UserInfoRepo;
import ir.ac.aut.hesabgar_group.request.AddingInvoiceRequest;
import ir.ac.aut.hesabgar_group.request.PayingInvoiceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InvoiceHelper {

    @Autowired
    private GroupInfoRepo groupInfoRepo;

    @Autowired
    private UserInfoRepo userInfoRepo;

    private String findMin(Map<String, Float> groupBalance) {
        float min = 0;
        String toBeReturn = null;
        for (String user : groupBalance.keySet()) {
            if (groupBalance.get(user) < min) {
                toBeReturn = user;
                min = groupBalance.get(user);
            }

        }
        return toBeReturn;
    }

    private String findMax(Map<String, Float> groupBalance) {
        float max = 0;
        String toBeReturn = null;
        for (String user : groupBalance.keySet()) {
            if (groupBalance.get(user) > max) {
                toBeReturn = user;
                max = groupBalance.get(user);
            }

        }
        return toBeReturn;
    }

    public GroupInfo findPaymentTerm(GroupInfo groupInfo, Map<String, Float> balance) {
//        getting members
        List<GroupMember> groupMembers = groupInfo.getMembers();
//        getting group balance
        Map<String, Float> groupBalance = new HashMap<>();
        for (GroupMember groupMember : groupMembers) {
            groupMember.setPaymentTerms(new ArrayList<>());
        }
        for (String s : groupInfo.getGroupBalance().keySet()) {
            groupBalance.put(s, groupInfo.getGroupBalance().get(s));
        }

        for (String user : groupBalance.keySet()) {
            for (String user2 : groupBalance.keySet()) {
                if (groupBalance.get(user).equals(groupBalance.get(user2) * -1) && !user.equals(user2) && !groupBalance.get(user).equals(0)) {

                    PaymentTerm paymentTermUser = new PaymentTerm(user2, groupBalance.get(user2));
                    PaymentTerm paymentTermUser2 = new PaymentTerm(user, groupBalance.get(user));

                    for (GroupMember groupMember : groupMembers) {
                        if (groupMember.getUserId().equals(user)) {
                            List<PaymentTerm> paymentTerms = groupMember.getPaymentTerms();
                            paymentTerms.add(paymentTermUser);
                        } else if (groupMember.getUserId().equals(user2)) {
                            List<PaymentTerm> paymentTerms = groupMember.getPaymentTerms();
                            paymentTerms.add(paymentTermUser2);
                        }
                    }

                    groupBalance.put(user,(float) 0);
                    groupBalance.put(user2,(float) 0);
                }
            }
        }
        while (findMin(groupBalance) != null) {
            String maxDebt = findMin(groupBalance);
            String maxCred = findMax(groupBalance);
            if (groupBalance.get(maxDebt) * -1 == groupBalance.get(maxCred)) {
                float minAmount = groupBalance.get(maxDebt);
                float maxAmount = groupBalance.get(maxCred);

                PaymentTerm paymentTermMaxDebt = new PaymentTerm(maxCred, groupBalance.get(maxDebt));
                PaymentTerm paymentTermMaxCred = new PaymentTerm(maxDebt, groupBalance.get(maxCred));

                for (GroupMember groupMember : groupMembers) {
                    if (groupMember.getUserId().equals(maxCred)) {
                        List<PaymentTerm> paymentTerms = groupMember.getPaymentTerms();
                        paymentTerms.add(paymentTermMaxCred);
                    } else if (groupMember.getUserId().equals(maxDebt)) {
                        List<PaymentTerm> paymentTerms = groupMember.getPaymentTerms();
                        paymentTerms.add(paymentTermMaxDebt);
                    }
                }
                groupBalance.put(maxDebt, minAmount + maxAmount);
                groupBalance.put(maxCred, minAmount + maxAmount);

            } else if (groupBalance.get(maxDebt) * -1 > groupBalance.get(maxCred)) {
                float minAmount = groupBalance.get(maxDebt);
                float maxAmount = groupBalance.get(maxCred);


                PaymentTerm paymentTermMaxDebt = new PaymentTerm(maxCred, maxAmount * -1);
                PaymentTerm paymentTermMaxCred = new PaymentTerm(maxDebt, maxAmount);

                for (GroupMember groupMember : groupMembers) {
                    if (groupMember.getUserId().equals(maxCred)) {
                        List<PaymentTerm> paymentTerms = groupMember.getPaymentTerms();
                        paymentTerms.add(paymentTermMaxCred);
                    } else if (groupMember.getUserId().equals(maxDebt)) {
                        List<PaymentTerm> paymentTerms = groupMember.getPaymentTerms();
                        paymentTerms.add(paymentTermMaxDebt);
                    }
                }

                groupBalance.put(maxDebt, minAmount + maxAmount);
                groupBalance.put(maxCred,(float) 0);

            } else if (groupBalance.get(maxDebt) * -1 < groupBalance.get(maxCred)) {
                float minAmount = groupBalance.get(maxDebt);
                float maxAmount = groupBalance.get(maxCred);


                PaymentTerm paymentTermMaxDebt = new PaymentTerm(maxCred, minAmount);
                PaymentTerm paymentTermMaxCred = new PaymentTerm(maxDebt, minAmount * -1);

                for (GroupMember groupMember : groupMembers) {
                    if (groupMember.getUserId().equals(maxCred)) {
                        List<PaymentTerm> paymentTerms = groupMember.getPaymentTerms();
                        paymentTerms.add(paymentTermMaxCred);
                    } else if (groupMember.getUserId().equals(maxDebt)) {
                        List<PaymentTerm> paymentTerms = groupMember.getPaymentTerms();
                        paymentTerms.add(paymentTermMaxDebt);
                    }
                }
                groupBalance.put(maxDebt,(float) 0);
                groupBalance.put(maxCred, minAmount + maxAmount);
            }


        }
        groupInfo.setMembers(groupMembers);
        return groupInfoRepo.save(groupInfo);

    }
    public GroupInfo findPaymentTermAfterPay(GroupInfo groupInfo,PayingInvoiceRequest payingInvoiceRequest) {
        List<GroupMember> groupMembers = groupInfo.getMembers();
        for (GroupMember groupMember : groupMembers) {
            groupMember.setPaymentTerms(new ArrayList<>());
        }
        for(GroupMember groupMember : groupInfo.getMembers()){
            if(groupMember.getUserId().equals(payingInvoiceRequest.getCreditorUserId())){
                List<PaymentTerm> paymentTerms = new ArrayList<>();
                for(PaymentTerm paymentTerm : groupMember.getPaymentTerms()){
                    if(!paymentTerm.getUser().equals(payingInvoiceRequest.getDebtorUserId())){
                        paymentTerms.add(paymentTerm);
                    }
                }
                groupMember.setPaymentTerms(paymentTerms);
            }
            if(groupMember.getUserId().equals(payingInvoiceRequest.getDebtorUserId())){
                List<PaymentTerm> paymentTerms = new ArrayList<>();
                for(PaymentTerm paymentTerm : groupMember.getPaymentTerms()){
                    if(!paymentTerm.getUser().equals(payingInvoiceRequest.getCreditorUserId())){
                        paymentTerms.add(paymentTerm);
                    }
                }
                groupMember.setPaymentTerms(paymentTerms);
            }
        }
        groupInfo.setMembers(groupMembers);
        return groupInfoRepo.save(groupInfo);

    }

    public GroupInfo makingNewInvoiceEvent(GroupInfo groupInfo, AddingInvoiceRequest addingInvoiceRequest) {
        List<AddingInvoiceEvent> addingInvoiceEvents = groupInfo.getAddingInvoiceEvents();
        if (addingInvoiceEvents == null) {
            addingInvoiceEvents = new ArrayList<>();
        }
        AddingInvoiceEvent addingInvoiceEvent = new AddingInvoiceEvent();

        addingInvoiceEvent.setInvoiceAdmin(addingInvoiceRequest.getUserId());

        addingInvoiceEvent.setTopic(addingInvoiceRequest.getTopic());
        addingInvoiceEvent.setDescription(addingInvoiceRequest.getDescription());


        List<String> eventMembers = new ArrayList<>();
        for (String user : addingInvoiceRequest.getGroupShare().keySet()) {
            eventMembers.add(user);
        }
        addingInvoiceEvent.setEventMembers(eventMembers);

        Map<String, Float> groupBalance = groupInfo.getGroupBalance();
        for (String user : addingInvoiceRequest.getGroupShare().keySet()) {
            float balance = addingInvoiceRequest.getGroupShare().get(user);
            groupBalance.put(user, balance + groupBalance.get(user));
        }

        Map<String, Float> eventBalance = new HashMap<>();

        for (String user : addingInvoiceRequest.getGroupShare().keySet()) {
            eventBalance.put(user, addingInvoiceRequest.getGroupShare().get(user));
        }
        addingInvoiceEvent.setEventBalance(eventBalance);

        addingInvoiceEvent.setCreationDate(new Date());

        addingInvoiceEvents.add(addingInvoiceEvent);

        groupInfo.setAddingInvoiceEvents(addingInvoiceEvents);


        return groupInfo;
    }

    public GroupInfo makingNewPayingEvent(GroupInfo groupInfo, PayingInvoiceRequest payingInvoiceRequest) {
        List<PayingInvoiceEvent> payingInvoiceEvents = groupInfo.getPayingInvoiceEvents();
        if (payingInvoiceEvents == null) {
            payingInvoiceEvents = new ArrayList<>();
        }
        PayingInvoiceEvent payingInvoiceEvent = new PayingInvoiceEvent();

        payingInvoiceEvent.setDebtor(payingInvoiceRequest.getDebtorUserId());
        payingInvoiceEvent.setCreditor(payingInvoiceRequest.getCreditorUserId());
        payingInvoiceEvent.setDebtAmount(payingInvoiceRequest.getDebtAmount());
        payingInvoiceEvent.setCreationDate(new Date());



        Map<String, Float> groupBalance = groupInfo.getGroupBalance();

        groupBalance.put(payingInvoiceRequest.getDebtorUserId(), groupBalance.get(payingInvoiceRequest.getDebtorUserId()) + payingInvoiceRequest.getDebtAmount());
        groupBalance.put(payingInvoiceRequest.getCreditorUserId(), groupBalance.get(payingInvoiceRequest.getCreditorUserId()) - payingInvoiceRequest.getDebtAmount());



        payingInvoiceEvents.add(payingInvoiceEvent);

        groupInfo.setPayingInvoiceEvents(payingInvoiceEvents);


        return groupInfo;
    }


    public void updateEventMembersBalance(AddingInvoiceRequest addingInvoiceRequest) {
        Map<String, Float> groupShare = addingInvoiceRequest.getGroupShare();
        for (String userId : groupShare.keySet()) {
            UserInfo userInfo = userInfoRepo.getUserInfoById(userId);
            float balance = userInfo.getOverallBalance();
            userInfo.setOverallBalance(balance + groupShare.get(userId));
            userInfoRepo.save(userInfo);
        }
    }

}
