package main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Classe permettant de contenir un commentaire
 *
 * @author ledru
 *
 */
public class Comment {

	private long ts; // is the comment's timestamp
	private long comment_id; // is the unique id of the comment
	private long user_id; // is the unique id of the user
	private String comment; //is a string containing the actual comment
	private String user; //is a string containing the actual user name
	private long comment_replied; //is the id of the comment being replied to (-1 if the tuple is a reply to a post)
	private long post_commented; //is the id of the post being commented (-1 if the tuple is a reply to a comment)

	private ArrayList<Integer> comments_associes= new ArrayList<Integer>();
	private int comment_score;
	/**
	 * Permet de charger les champs d'un objet commentaire avec un string formatée comme suit :
	 * 2010-02-09T04:05:20.777+0000|529590|2886|LOL|Baoping Wu||529360
	 * @param s
	 */
	public void loadFromString(String s){


			String time = s.substring(0,s.indexOf("|"));
			s =  s.replace(time+"|","");
			time = time.replace("T", " ");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			try {
				ts = dateFormat.parse(time).getTime();

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String comment_id = s.substring(0, s.indexOf("|"));

			this.comment_id = Long.parseLong(comment_id);

			s =  s.replace(comment_id+"|","");

			String user_id = s.substring(0, s.indexOf("|"));

			this.user_id = Long.parseLong(user_id);

			s =  s.replace(user_id+"|","");

			String comment = s.substring(0, s.indexOf("|"));

			this.comment = comment;

			s =  s.replace(comment+"|","");

			String user = s.substring(0, s.indexOf("|"));

			this.user = user;

			s =  s.replace(user+"|","");

			String comment_replied = s.substring(0, s.indexOf("|"));

			if(comment_replied.length() > 0){
			this.comment_replied = Long.parseLong(comment_replied);
			}
			else{
			this.comment_replied = 0;
			}

			s =  s.replace(comment_replied+"|","");

			String post_commented = s;

			if(post_commented.length() > 0){
			this.post_commented = Long.parseLong(post_commented);
			}
			else{
			this.post_commented =0;
			}
	}

	public int calculScore(long tempsSysteme){
		if(this.comment_score==0){
			return 0;
		}else{
			int score=10;
			int nb_jour=(int)((tempsSysteme-ts)/86_400_000);
			score-=nb_jour;

			for(int i=0;i<comments_associes.size();i++){
				score+=(Ordonnanceur.comments.get(comments_associes.get(i))).calculScore(tempsSysteme);

			}
			this.comment_score=score;

				return score;
		}
	}

	public void affecter(Comment comment){

		this.comment_id=comment.getComment_id();
		this.ts=comment.getTs();
		this.comment=comment.getComment();
		this.comment_replied=comment.getComment_replied();
		this.user=comment.getUser();
		this.user_id=comment.getUser_id();
		this.post_commented=comment.getPost_commented();



	}



	public ArrayList<Integer> getComments_associes() {
		return comments_associes;
	}

	public void setComments_associes(ArrayList<Integer> comments_associes) {
		this.comments_associes = comments_associes;
	}

	public Comment() {
		super();
		this.comment_score=10;
	}
	@Override
	public String toString() {
		return "Comment [ts=" + ts + ", comment_id=" + comment_id
				+ ", user_id=" + user_id + ", comment=" + comment + ", user="
				+ user + ", comment_replied=" + comment_replied
				+ ", post_commented=" + post_commented + "]";
	}
	public long getTs() {
		return ts;
	}
	public void setTs(long ts) {
		this.ts = ts;
	}
	public long getComment_id() {
		return comment_id;
	}
	public void setComment_id(long comment_id) {
		this.comment_id = comment_id;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
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
	public long getComment_replied() {
		return comment_replied;
	}
	public void setComment_replied(long comment_replied) {
		this.comment_replied = comment_replied;
	}
	public long getPost_commented() {
		return post_commented;
	}
	public void setPost_commented(long post_commented) {
		this.post_commented = post_commented;
	}


}
