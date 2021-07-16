package ir.ac.aut.hesabgar_group.domain.data;

public class BankAccount {
    private String bankName;
    private String bankAccountNumber;
    private String atmCardNumber;

    public BankAccount(String bankName, String bankAccountNumber, String atmCardNumber) {
        this.bankName = bankName;
        this.bankAccountNumber = bankAccountNumber;
        this.atmCardNumber = atmCardNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getAtmCardNumber() {
        return atmCardNumber;
    }

    public void setAtmCardNumber(String atmCardNumber) {
        this.atmCardNumber = atmCardNumber;
    }
}
