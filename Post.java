package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Post {

	private Timestamp ts;
	private int post_id;
	private int user_id;
	private String post;
	private String user;



	public static void main(String[] args) throws IOException{
		loadfile();
	}



	public static void loadfile() throws IOException{

		FileReader reader = new FileReader("posts.txt");
		BufferedReader bufferedreader = new BufferedReader(reader);
		StringBuffer buffer = new StringBuffer();
		String line;
	line = bufferedreader.readLine();

//----------- Découpage de la ligne
	String timstamp;

	timstamp = line.substring(0,line.indexOf("|"));



	String reste;
	reste = line.substring(line.indexOf("|")+1,line.length());

int post_id;

	post_id=Integer.parseInt(reste.substring(0,reste.indexOf("|")));


	reste=reste.substring(reste.indexOf("|")+1,reste.length());

	int user_id;

	user_id=Integer.parseInt(reste.substring(0,reste.indexOf("|")));
	reste=reste.substring(reste.indexOf("|")+1,reste.length());

	String contenu_post;

	contenu_post=reste.substring(0,reste.indexOf("|"));
	reste=reste.substring(reste.indexOf("|")+1,reste.length());


	String user_name;

	user_name=reste.substring(0,reste.length());
	reste=reste.substring(reste.indexOf("|")+1,reste.length());


//----\Fin dsécoupage de la ligne


// ---- Conversion en Date------


	try{
		timstamp=timstamp.replace("T", " ");
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
	   Date parsedDate = dateFormat.parse(timstamp);
	  // Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
	    System.out.println(parsedDate.getTime());
	}catch(Exception e){//this generic but you can control another types of exception
				System.out.println(e);
	}
	//-------\Fin conversion en date




	}





	public Post(Timestamp ts, int post_id, int user_id, String post, String user) {
		super();
		this.ts = ts;
		this.post_id = post_id;
		this.user_id = user_id;
		this.post = post;
		this.user = user;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public Timestamp getTs() {
		return ts;
	}
	public void setTs(Timestamp ts) {
		this.ts = ts;
	}
	public int getPost_id() {
		return post_id;
	}
	public void setPost_id(int post_id) {
		this.post_id = post_id;
	}



}
