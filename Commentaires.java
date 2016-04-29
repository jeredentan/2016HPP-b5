package main;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe permettant de contenir un commentaire
 * 
 * @author ledru
 *
 */
public class Commentaires {

	private Date ts; // is the comment's timestamp
	private int comment_id; // is the unique id of the comment
	private int user_id; // is the unique id of the user
	private String comment; //is a string containing the actual comment
	private String user; //is a string containing the actual user name
	private int comment_replied; //is the id of the comment being replied to (-1 if the tuple is a reply to a post)
	private int post_commented; //is the id of the post being commented (-1 if the tuple is a reply to a comment)
	
	
	/**
	 * Permet de charger les champs d'un objet commentaire avec un string fomaté comme suit :
	 * 2010-02-09T04:05:20.777+0000|529590|2886|LOL|Baoping Wu||529360
	 * @param s
	 */
	public void loadFromString(String s){
			String time = s.substring(0,s.indexOf("|"));
			s =  s.replace(time+"|","");
			time = time.replace("T", " ");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			try {
				ts = dateFormat.parse(time);
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
			String comment_id = s.substring(0, s.indexOf("|"));
			
			this.comment_id = Integer.parseInt(comment_id);

			s =  s.replace(comment_id+"|","");

			String user_id = s.substring(0, s.indexOf("|"));
			
			this.user_id = Integer.parseInt(user_id);
			
			s =  s.replace(user_id+"|","");

			String comment = s.substring(0, s.indexOf("|"));
			
			this.comment = comment;
			
			s =  s.replace(comment+"|","");

			String user = s.substring(0, s.indexOf("|"));
			
			this.user = user;
			
			s =  s.replace(user+"|","");

			String comment_replied = s.substring(0, s.indexOf("|"));
			
			if(comment_replied.length() > 0){
			this.comment_replied = Integer.parseInt(comment_replied);
			}
			else{
			this.comment_replied = 0;
			}
			
			s =  s.replace(comment_replied+"|","");
			
			String post_commented = s;
			
			if(post_commented.length() > 0){
			this.post_commented = Integer.parseInt(post_commented);
			}
			else{
			this.post_commented = 0;
			}
			
			
	}

	public Commentaires(Timestamp ts, int comment_id, int user_id, String comment,
			String user, int comment_replied, int post_commented) {
		super();
		this.ts = ts;
		this.comment_id = comment_id;
		this.user_id = user_id;
		this.comment = comment;
		this.user = user;
		this.comment_replied = comment_replied;
		this.post_commented = post_commented;
	}
	@Override
	public String toString() {
		return "Comment [ts=" + ts + ", comment_id=" + comment_id
				+ ", user_id=" + user_id + ", comment=" + comment + ", user="
				+ user + ", comment_replied=" + comment_replied
				+ ", post_commented=" + post_commented + "]";
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
	public int getComment_id() {
		return comment_id;
	}
	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public int getComment_replied() {
		return comment_replied;
	}
	public void setComment_replied(int comment_replied) {
		this.comment_replied = comment_replied;
	}
	public int getPost_commented() {
		return post_commented;
	}
	public void setPost_commented(int post_commented) {
		this.post_commented = post_commented;
	}

	
}	
