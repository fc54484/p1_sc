public class SellInfo {
    String name;
    int quantidade;
    int price;

    public SellInfo(String name, int quantidade, int price){
        this.name=name;
        this.quantidade=quantidade;
        this.price=price;
    }

    public String toString(){
        return name+"/"+quantidade+"/"+price;
    }
}
