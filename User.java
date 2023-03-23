import java.io.*;
import java.net.*;
import java.util.*;

public class User {
    String name;
    int saldo;
    List<SellInfo> vinho;
    // List<String>msg;

    public User(String name){
        this.name=name;
        this.saldo=200;
        this.vinho=new ArrayList<SellInfo>();
        // this.msg=new ArrayList<>();
    }

    SellInfo getWine(String wineName){
        SellInfo res=null;
        for(int i=0;i<this.vinho.size();i++){
            if(this.vinho.get(i).name.equals(wineName)){
                res=this.vinho.get(i);
            }
        }
        return res;
    }

    public String toString(){
        String sellerStr="";
        // String mail="";
        if(vinho.size()!=0){
            for(int i=0;i<vinho.size()-1;i++){
                sellerStr+=vinho.get(i).toString();
                sellerStr+=",";
            }
            sellerStr+=vinho.get(vinho.size()-1).toString();
        }

        // if(msg.size()!=0){
        //     for(int j=0;j<msg.size()-1;j++){
        //         mail+=msg.get(j);
        //         mail+="/";
        //     }
        //     mail+=msg.get(msg.size()-1);
        // }
        return name+";"+saldo+";"+sellerStr+"\n";
    }
}
