import java.io.*;
import java.net.*;
import java.util.*;

public class Wine {
    String name;
    String image;
    int star;
    int quantidade;
    List<SellInfo> seller;

    public Wine(String name, String image){
        this.name=name;
        this.image=image;
        this.star=0;
        this.quantidade=0;
        this.seller=new ArrayList<>();
    }

    SellInfo getSeller(String vendedor){
        SellInfo res=null;
        for(int i=0;i<this.seller.size();i++){
            if(this.seller.get(i).name.equals(vendedor)){
                res=this.seller.get(i);
            }
        }
        return res;
    }

    public String toString(){
        String sellerStr="";
        if(seller.size()!=0){
            for(int i=0;i<seller.size()-1;i++){
                sellerStr+=seller.get(i).toString();
                sellerStr+=",";
            }
            sellerStr+=seller.get(seller.size()-1).toString();
        }
        return name+";"+image+";"+star+";"+quantidade+";"+sellerStr+"\n";
    }

    public static void main(String[] args) {
        // Wine w=new Wine("vinho", "hh");
        // SellInfo s=new SellInfo("12", 0, 0);
        // w.seller.add(s);
        // w.seller.add(s);
        // System.out.println(w.toString());
    }
}
