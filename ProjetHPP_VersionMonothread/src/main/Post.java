package main;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Post {

	private long ts;
	private  int post_id;
	private  int user_id;
	private  String contenu_post;
	private  String user;

	private ArrayList<Integer> comments_associes= new ArrayList<Integer>();
	private int post_score;


	public  void loadFromString(String line) throws IOException{



//----------- Découpage de la ligne
	String timstamp;

	timstamp = line.substring(0,line.indexOf("|"));


	String reste;
	reste = line.substring(line.indexOf("|")+1,line.length());

this.post_id=Integer.parseInt(reste.substring(0,reste.indexOf("|")));
	reste=reste.substring(reste.indexOf("|")+1,reste.length());

	this.user_id=Integer.parseInt(reste.substring(0,reste.indexOf("|")));
	reste=reste.substring(reste.indexOf("|")+1,reste.length());

	this.contenu_post=reste.substring(0,reste.indexOf("|"));
	reste=reste.substring(reste.indexOf("|")+1,reste.length());


this.user=reste.substring(0,reste.length());
	reste=reste.substring(reste.indexOf("|")+1,reste.length());


//----\Fin dsécoupage de la ligne



// ---- Conversion en Date------

	try{
		timstamp=timstamp.replace("T", " ");
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
	   Date parsedDate = dateFormat.parse(timstamp);
this.ts=parsedDate.getTime();
	  // Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());

	}catch(Exception e){//this generic but you can control another types of exception
				System.out.println(e);
	}
//-------\Fin conversion en date
	}



	public int calculScore(long tempsSysteme){
		if(this.post_score==0){
			return 0;
		}else{
			int score=10;
			int nb_jour=(int)((tempsSysteme-ts)/86_400_000);

			score-=nb_jour;
			for(int i=0;i<comments_associes.size();i++){
				score+=(Ordonnanceur.comments.get(comments_associes.get(i))).calculScore(tempsSysteme);


			}
			this.post_score=score;
				return score;
		}
	}



	public ArrayList<Integer> getComments_associes() {
		return comments_associes;
	}



	@Override
	public String toString() {
		return "Post [ts=" + ts + ", post_id=" + post_id + ", user_id=" + user_id + ", contenu_post=" + contenu_post
				+ ", user=" + user + ", comments_associes=" + comments_associes + ", post_score=" + post_score + "]";
	}



	public void setComments_associes(ArrayList<Integer> comments_associes) {
		this.comments_associes = comments_associes;
	}



	public Post() {
		super();


	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getPost() {
		return contenu_post;
	}
	public void setPost(String post) {
		this.contenu_post = post;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public long getTs() {
		return ts;
	}
	public void setTs(long ts) {
		this.ts = ts;
	}
	public int getPost_id() {
		return post_id;
	}
	public void setPost_id(int post_id) {
		this.post_id = post_id;
	}

	public int getPost_score() {
		return post_score;
	}
	public void setPost_score(int post_score) {
		this.post_score = post_score;
	}



	public void affecter(Post currentPost) {
this.contenu_post=currentPost.getPost();
this.post_id=currentPost.getPost_id();
this.ts=currentPost.getTs();
this.user=currentPost.getUser();
this.user_id=currentPost.getUser_id();
this.post_score=10;

	}
}