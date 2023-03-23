import java.io.*;
import java.net.*;
import java.util.*;

//Servidor myServer

public class server{
	

	public static void main(String[] args) {
		System.out.println("servidor: main");
		server server = new server();
		server.startServer();
		
	}

	public void startServer (){
		ServerSocket sSoc = null;
        
		try {
			sSoc = new ServerSocket(12345);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}
         
		while(true) {
			try {
				Socket inSoc = sSoc.accept();
				ServerThread newServerThread = new ServerThread(inSoc);
				newServerThread.start();
		    }
		    catch (IOException e) {
		        e.printStackTrace();
		    }
		    
		}
		//sSoc.close();
	}


	//Threads utilizadas para comunicacao com os clientes
	class ServerThread extends Thread {
		String user;
		String pwd;
		
		private Socket socket = null;

		ServerThread(Socket inSoc) {
			socket = inSoc;
			System.out.println("thread do server para cada cliente");
		}
 
		public void run(){
			try {
				ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());

				// user = null;
				// pwd = null;
				String op = null;
			
				try {
					this.user = (String)inStream.readObject();
					this.pwd = (String)inStream.readObject();
					System.out.println("thread: depois de receber a password e o user");
				}catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}

                int logState = isExist();
                outStream.writeObject(logState);
				
				boolean loop=true;
				while(loop){
					try {
						op = (String)inStream.readObject();
						String res=procOp(op);
						outStream.writeObject(res);
					} catch (Exception e) {
					}
					// outStream.close();
					// inStream.close();
					// socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

        public int isExist() {
            try {
                File myObj = new File("info.txt");
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                  String data = myReader.nextLine();
                  String[] dataArr=data.split(":");
				  
				  //login com sucesso
                  if(dataArr[0].equals(user) && dataArr[1].equals(pwd)){
                    return 0;
                  }
				  //wrong password
				  else if(dataArr[0].equals(user) && !dataArr[1].equals(pwd)){
                    return 1;
                  }
                }

				User u=new User(user);
				writeFile("user.txt", u.toString());
				
				writeFile("info.txt", "\n"+user+":"+pwd);

                myReader.close();
            } catch (Exception e) {
			}
			return -1;
        }


		public String procOp(String op){
			String res="";
			String[] command = op.split("\\s+");
			switch(command[0]) {
				case "add": case "a":
				  if(command.length!=3){
					res="Invalid command.";
				  }else{
					res=add(command[1],command[2]);
				  }
				  break;
				case "sell": case "s":
				  if(command.length!=4){
					res="Invalid command.";
				  }else{
					res=sell(command[1],command[2],command[3]);
				  }
				  break;
				case "view": case "v":
				  if(command.length!=2){
					res="Invalid command.";
				  }else{
					res=view(command[1]);
				  }
				  break;
				case "buy": case "b":
				  if(command.length!=4){
					res="Invalid command.";
				  }else{
					res=buy(command[1],command[2],command[3]);
				  }
				  break;
				case "wallet": case "w":
				  if(command.length!=1){
					res="Invalid command.";
				  }else{
					res="Balance: "+wallet();
				  }
				  break;
				case "classify": case "c":
				  break;
				case "talk": case "t":
				  break;
				case "read": case "r":
				  break;
				default:
				  res="This op does not exist.";
			}
			return res;
		}

		public String add(String name, String image){
			String res="";
			try {
				File fig = new File(image);
				boolean hasImage=fig.exists();
				if(hasImage){
					String wineInF=search("wine.txt",name);
					if(wineInF==null){
						Wine wine=new Wine(name, image);
						writeFile("wine.txt", wine.toString());
						res="Successful.";
					}else{
						res="Wine already exists.";
					}
				}else{
					res="Image does not exist.";
				}
			} catch (Exception e) {}
			return res;
		}

		public String sell(String name, String value, String quantity){
			String res="";
			int price=Integer.parseInt(value);
			int quan=Integer.parseInt(quantity);

			String wineInfo=search("wine.txt",name);
			if(wineInfo==null){
				res="Wine does not exist.";
			}else{
				Wine wine=procWine(wineInfo);
				wine.quantidade+=quan;

				User u=procUser(search("user.txt", this.user));
				boolean hasVinho=false;

				for(int i=0;i<u.vinho.size();i++){
					//如果用户已经在卖这个酒（默认每次价格一样
					if(u.vinho.get(i).name.equals(name)){
						u.vinho.get(i).quantidade+=quan;
						hasVinho=true;
					}
				}
				if(!hasVinho){
					u.vinho.add(new SellInfo(name, quan, price));
				}
				modifyFile("user.txt", search("user.txt", this.user), u.toString());

				
				SellInfo oldS = wine.getSeller(this.user);
				if(oldS==null){
					wine.seller.add(new SellInfo(this.user, quan, price));
					modifyFile("wine.txt",wineInfo,wine.toString());
				}else{
					SellInfo newS = oldS;
					newS.quantidade=u.getWine(name).quantidade;
					String newInfo=wine.toString().replaceAll(oldS.toString(), newS.toString());
					modifyFile("wine.txt",wineInfo,newInfo);
				}
				
				res="Sell successful.";
			}
			return res;
		}

		public String view(String name){
			String res="";
			String wineInfo=search("wine.txt",name);
			if(wineInfo==null){
				res="Wine does not exist.";
			}else{
				Wine aim=procWine(wineInfo);

				res="Wine: "+aim.name+"\n"
				+"Classification: "+aim.star+"\n";
				if(aim.seller.size()!=0){
					res+="Information of Seller:\n";
					for(int i=0;i<aim.seller.size();i++){
						res+="Seller: "+aim.seller.get(i).name+"\n"
						+"Quantity: "+aim.seller.get(i).quantidade+"\n"
						+"Price: "+aim.seller.get(i).price+"\n"+"----------";
					}
				}
		
			}
			return res;
		}

		public String buy(String wineName, String seller, String quantity){
			String res="";
			if(search("wine.txt", wineName)==null){
				res="Wine does not exist.";
			}else{
			
				Wine w=procWine(search("wine.txt", wineName));
				User comprador=procUser(search("user.txt", this.user));
				User vendedor=procUser(search("user.txt", seller));

				int num=Integer.parseInt(quantity);
				int total=vendedor.getWine(wineName).price*num;

				if(vendedor.getWine(wineName).quantidade>=num){
					if(comprador.saldo<total){
						res="Insufficient balance.";
					}else{
						//买家余额减少
						comprador.saldo-=total;
						System.out.println(search("user.txt", this.user));
						System.out.println(comprador.toString());
						modifyFile("user.txt", search("user.txt", this.user), "\n"+comprador.toString());


						//卖家余额增加 wine减少
						vendedor.saldo+=total;
						vendedor.getWine(wineName).quantidade-=num;
						modifyFile("user.txt", search("user.txt", seller), vendedor.toString());


						//酒数量变少 卖家信息变动
						w.quantidade-=num;
						for(int i=0;i<w.seller.size();i++){
							if(w.seller.get(i).name.equals(seller)){
								w.seller.get(i).quantidade-=num;
							}
						}
						modifyFile("wine.txt", search("wine.txt", wineName), w.toString());
						res="Buy successful.";
					}
				}else{
					res="Out of stock.";
				}
			}

			return res;
		}

		public String wallet(){
			User u=procUser(search("user.txt", this.user));
			return String.valueOf(u.saldo);
		}
			

		//将文档中的信息转变成user
		public User procUser(String userInfo){
			
			String[] infoArr= userInfo.split(";");
			User res=new User(infoArr[0]);
			res.saldo=Integer.parseInt(infoArr[1]);

			if(infoArr.length==3){
				res.vinho=procSell(infoArr[2]);
			}
			return res;
		}
			
		public Wine procWine(String wineInfo){
			String[] infoArr=wineInfo.split(";");
			Wine res=new Wine(infoArr[0], infoArr[1]);
			res.star=Integer.parseInt(infoArr[2]);
			res.quantidade=Integer.parseInt(infoArr[3]);
			if(infoArr.length==5){
				res.seller=procSell(infoArr[4]);
			}

			return res;
		}
			
		public List<SellInfo> procSell(String sellDetail){
			List<SellInfo> res=new ArrayList<>();
			
			String[] venda=sellDetail.split(",");
			
			for(String x:venda){
				String[] detail=x.split("/");
				SellInfo s=new SellInfo(detail[0], Integer.parseInt(detail[1]), Integer.parseInt(detail[2]));
				res.add(s);
			}
			return res;
		}


		public String search(String fileName, String name){
			try {
				File f = new File(fileName);
				Scanner reader = new Scanner(f);
				while (reader.hasNextLine()) {
					String info = reader.nextLine();
					String[] infoArr=info.split(";");
					if(infoArr[0].equals(name)){
						return info;
					}
				}
			} catch (Exception e) {
			}
			return null;
		}

		public void writeFile(String fileName, String data){
			try {
				FileWriter writer = new FileWriter(fileName,true);
				writer.write(data);
				writer.close();
			} catch (Exception e) {}
		}

		public void modifyFile(String fileName, String oldStr, String newStr){
			File f=new File(fileName);
			String oldContent = "";
			BufferedReader reader = null;

			try {
				reader=new BufferedReader(new FileReader(f));
				String line=reader.readLine();
				while (line != null){
					oldContent = oldContent + line;
					line = reader.readLine();
				}
				String newContent = oldContent.replaceAll(oldStr, newStr);
				FileWriter writer = new FileWriter(f);
				writer.write(newContent);
				writer.close();
			} catch (Exception e) {
			}
		}

	}
}