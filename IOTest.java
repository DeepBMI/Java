import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



class Link{
	private class Node{
		private String data;
		private Node next ;
		public Node(String data) {
			this.data = data ;
		}
		public void addNode(Node newNode) {
			if(this.next == null) {
				this.next = newNode ;
			}else {
				this.next.addNode(newNode) ;
			}
		}
		public Boolean containsNode(String data) {

			if(data.equals(this.data)) {
				return true ;
			}else {
				if(this.next != null) {
					return this.next.containsNode(data) ;
				}else {
					return false ;
				}
			}
		}
		public String getNode(int index) {
			if(Link.this.foot++ == index) {
				return this.data ;
			}else {
				return this.next.getNode(index) ;
			}
		}
		public void setNode(int index, String data) {
			if(Link.this.foot++ == index) {
				this.data = data ;
			}else {
				this.next.setNode(index,data) ;
			}
		}
		public void removeNode(Node previous, String data) {
			if(data.equals(this.data)) {
				previous.next = this.next ; 
			}else {
				this.next.removeNode(this, data);
			}
		}
		public void toArrayNode() {
			Link.this.retArray[Link.this.foot++] = this.data ;
			if(this.next != null) {
				this.next.toArrayNode();
			}
		}
	}
//==================================内部类==============================================//
	private Node root ;
	private int count = 0 ;
	private int foot = 0 ;
	private String [] retArray ;
	
	public void add(String data) {
		if(data == null) {
			return ;
		}
		Node newnode = new Node(data) ;
		if(this.root == null) {
			this.root = newnode ;
		}else {
			this.root.addNode(newnode) ;
		}
		this.count++;
	}
	public int size() {
		return this.count;
	}
	public Boolean isEmpty() {
		return this.count == 0 ;
		}
	public Boolean contains(String data) {
		if(data == null || this.root == null) {
			return false ;
		}else {
			return this.root.containsNode(data) ;
		}
	}
	public String get(int index) {
		if(index > this.count) {
			return null;
		}else {
			this.foot = 0 ;
			return this.root.getNode(index) ;
		}
	}
	public void set(int index, String data) {
		if(index >= this.count) {
			return ;
		}else {
			this.foot = 0 ;
			this.root.setNode(index, data);
		}
	}
	public void remove(String data) {
		if(this.contains(data)) {
			if(data.equals(this.root.data)) {
				this.root = this.root.next ;
			}else {
				this.root.next.removeNode(this.root, data);
			}
			this.count--;
		}
	}
	public String[] toArray() {
		if(this.count == 0) {
			return null ;
		}
		this.foot = 0;
		this.retArray = new String[this.count] ;
		this.root.toArrayNode();
		return this.retArray ;
	}
	public void clear() {
		this.root = null ;
		this.count = 0 ;
	}
}

class Tuple extends Link{
	public static  String[] title ;
	private Link [] items;
	public Tuple() {
		items = new Link[this.title.length] ;
		}
	public void loadData(String[] data) {
		for(int x = 0; x < data.length; x++) {
			items[x] = new Link();
			if(data[x].contains(",")) {
				if(data[x].contains("\"")) {
				data[x] = data[x].substring(1, data[x].length()-1); //去掉双引号
				}
				String[] temp = data[x].split(",");
				for(int y = 0; y < temp.length; y++) {
					while(temp[y].startsWith(" ")) {
						temp[y] = temp[y].substring(1, temp[y].length());//去掉首空格
					}
					this.items[x].add(temp[y]);
				}
				
			}else {	
				this.items[x].add(data[x]);
			}
		}
		
	}
	
	public int getIndexOfTitle(String name) {
		for(int x = 0; x < title.length; x++) {
			if(name.equals(title[x])) {
				return x;
			}
		}
		return -1;
	}
	public void removeduplicate(String name) {	  //去重
		int index = this.getIndexOfTitle(name);
		int count = this.items[index].size();
		if(count > 1) {
			for(int x = 0 ; x < count; x++) {
				String temp = this.items[index].get(0);  //每次从头拿
				this.items[index].remove(temp);
				while(this.items[index].contains(temp)) {
					this.items[index].remove(temp);
				}
				this.items[index].add(temp);			 //插入队尾
			}
		}
	}
	public String extract() {
		String row = null;
		String[] column = new String[this.title.length];
		for(int x = 0; x < column.length; x++) {
			if(this.items[x].size() > 1) {
				String [] temp = this.items[x].toArray();
				String temp1 = String.join(",", temp);
				temp1 = "\"" + temp1 + "\"";
				column[x] = temp1;
			}else {
				column[x] = this.items[x].get(0);
			}
		}
		row = String.join(",", column);
		return row;
	}
	public void print1(Link l) {
			String[] s = (String[])l.toArray();
			for(String q : s) {
				System.out.print("dddd"+q);
			
		}
		System.out.println("");
	}
	public void print() {
		for(int x = 0; x < this.items.length; x++){
			String[] s = (String[])this.items[x].toArray();
			
			for(String q : s) {
				System.out.print(q);
			}
			
		}
		System.out.println("");
	}
	
}
///*****************************************************************************
class Interface extends JFrame implements ActionListener{
	List<Tuple> table = new ArrayList<>();
	private String filepath; 
	JFrame f = new JFrame("Excel列表删重程序");
	JMenuBar mb;    
	JMenu file;    
	JMenuItem open;    
	JList ta;
	JProgressBar jb;
public Interface() {
	// TODO Auto-generated constructor stub 
	open=new JMenuItem("Open File");    
	open.addActionListener( this);            
	file=new JMenu("File");    
	file.add(open); 
	jb=new JProgressBar(0,2000);    
	jb.setBounds(40,40,160,30);         
	jb.setValue(0);    
	jb.setStringPainted(true);
	mb=new JMenuBar();    
	mb.setBounds(0,0,800,20);    
	mb.add(file);              
	ta=new JList();    
	ta.setBounds(0,20,800,800);              
	f.add(mb);    
	f.add(ta); 
	f.add(jb);
    f.setSize(300,400);    
    f.setVisible(true);
	}    
	public void actionPerformed(ActionEvent e) {    
	if(e.getSource()==open){    
	    JFileChooser fc=new JFileChooser();    
	    int i=fc.showOpenDialog(this);    
	    if(i==JFileChooser.APPROVE_OPTION){    
	        File f=fc.getSelectedFile();
	        this.filepath = f.getPath();
	        table = openFile(f.getPath());  
	        
	    }    
	}
}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//List<LinkTable> Students = new ArrayList<LinkTable>();
		String SouceFile = "D:\\javatest\\实验三-数据.csv" ;
		String DestFile = "D:\\javatest\\123.csv" ;
		List<Tuple> table = new ArrayList<>();
		table = openFile(SouceFile);
		Interface jm = new Interface() ;
		//cleanRepeter(table, "星期一");
		//creatFile(DestFile);
		//writeFile(DestFile,table);
		
		}
	
		public static void cleanRepeter(List<Tuple> table,String name) {
			Iterator<Tuple> it = table.iterator();
			while(it.hasNext()) {
				it.next().removeduplicate(name);
			}
		}
		public static List<Tuple> openFile(String sf) {
			File file = new File(sf) ;
			List<Tuple> table = new ArrayList<>();
			BufferedReader br = null ;
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(sf),"utf-8")) ;
				String line = null ;
				Tuple.title = br.readLine().split(",") ;
				Tuple.title[0] = Tuple.title[0].substring(1, Tuple.title[0].length());
				while((line=br.readLine()) != null) {
					String[] temp = line.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)",-1) ;
					Tuple tuple = new Tuple();
					tuple.loadData(temp);
					table.add(tuple);
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			finally{
				try {
					br.close();
				}catch (Exception e2){
					br = null ;
					System.out.println(e2.getMessage());
				}
			}
			return table;
		}
		public static void creatFile(String dest) {
			File f = new File(dest);
			try {
				f.createNewFile();
				f.mkdir();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		} 
		public static void writeFile(String df,List<Tuple> table) {
			File file = new File(df) ;
			BufferedWriter bw = null ;
			String title = String.join(",",Tuple.title);
			Iterator<Tuple> it = table.iterator();
			try {
				bw = new BufferedWriter(new FileWriter(df,true)) ;
				bw.write(title, 0, title.length());
				bw.newLine();
				while(it.hasNext()) {
					String temp = it.next().extract();
					bw.write(temp+"\n", 0, temp.length());
					bw.newLine();
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e.getMessage());
			}
			finally {
				try {
					bw.close();
				} catch (Exception e2) {
					// TODO: handle exception
					bw = null ;
					System.out.println(e2.getMessage());
				}
			}
		}
}
