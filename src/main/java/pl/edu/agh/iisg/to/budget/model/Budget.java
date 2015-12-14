package pl.edu.agh.iisg.to.budget.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.math.BigDecimal;

/**
 * Created by tom on 23.11.15.
 */
public class Budget {

    private ObjectProperty<Category> category;
    private ObjectProperty<BigDecimal> amount;
    private ObjectProperty<BigDecimal> spent;
    private ObjectProperty<BigDecimal> balance;


    public Budget(BigDecimal amount) {
        this.amount = new SimpleObjectProperty<>(amount);
        this.spent =  new SimpleObjectProperty<>(new BigDecimal(0));
        this.balance = getBalance();
    }

    public Budget() {
        this.amount = new SimpleObjectProperty<>(new BigDecimal(0));
        this.spent =  new SimpleObjectProperty<>(new BigDecimal(0));
        this.balance = getBalance();
    }

    public ObjectProperty<BigDecimal> getAmount() {
        return amount;
    }

    public Budget setAmount(BigDecimal amount) {
        this.amount = new SimpleObjectProperty<>(amount);
        this.balance.setValue(amount.subtract(this.spent.getValue()));
        return this;
    }

    public ObjectProperty<BigDecimal> amountProperty() {
        return amount;
    }

    public ObjectProperty<BigDecimal> getSpent() {
        return spent;
    }

    public Budget setSpent(BigDecimal spent) {
        this.spent = new SimpleObjectProperty<>(spent);
        this.balance.setValue(this.amount.getValue().subtract(spent));
        return this;
    }

    public ObjectProperty<Category> getCategory() {
        return category;

    }

    public Budget setCategory(Category category) {
        this.category = new SimpleObjectProperty<>(category);
        return this;
    }

    public ObjectProperty<BigDecimal> getBalance() {
        this.balance = new SimpleObjectProperty<>(this.amount.get().subtract(this.spent.get()));
        return this.balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance.set(balance);
    }


    /* dla danego bud�etu dosta� sumaryczny bilans - czyli jak jaki� bilans ma podkategorie to ten bilans musi sumowa� wszystkie podkategorie
    Czyli taki bilans razem z podkategoriami Emotikon smile
*/

//    public BigDecimal getSummarizedBalance(){
//        return getTotalAmount().subtract(getTotalSpent());
//    }
//    public BigDecimal getTotalAmount(){
//        Category category = this.category.get();
//        BigDecimal sum = new BigDecimal(0).add(this.amount.get());
//        if(category.getSubCategories()!=null && !category.getSubCategories().isEmpty()){
//            for(ObjectProperty<Category> categoryOP : category.getSubCategories()){
//                sum.add(categoryOP.get().getBudget().get().getAmount().get());
//            }
//        }
//        return sum;
//    }
//
//    public BigDecimal getTotalSpent(){
//        Category category = this.category.get();
//        BigDecimal sum = new BigDecimal(0).add(this.amount.get());
//        if(category.getSubCategories()!= null && !category.getSubCategories().isEmpty()){
//            for(ObjectProperty<Category> categoryOP : category.getSubCategories()){
//                sum.add(categoryOP.get().getBudget().get().getSpent().get());
//            }
//        }
//        return sum;
//    }
}
