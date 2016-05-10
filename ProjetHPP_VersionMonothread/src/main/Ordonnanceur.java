package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Ordonnanceur {
	public static ArrayList<Post> posts = new ArrayList<Post>();
	public static ArrayList<Comment> comments = new ArrayList<Comment>();
	public static ArrayList<Post> top3_posts= new ArrayList<Post>();
	private long Date;
	private Post currentPost;
	private Comment currentComment;
	private FileReader filepost;
	private FileReader filecomment;
	private BufferedReader bufferedReaderpost;
	private BufferedReader bufferedReadercomment;
	private Boolean bool1=false;
	private Boolean bool2=false;



	public Ordonnanceur(){
		currentPost= new Post();
		currentComment= new Comment();
		try {
			filepost= new FileReader("/Users/Jeredentan/Desktop/posts.txt");
			bufferedReaderpost   = new BufferedReader(filepost);
			filecomment= new FileReader("/Users/Jeredentan/Desktop/comments.txt");
			bufferedReadercomment   = new BufferedReader(filecomment);

			top3_posts.add(new Post());
			top3_posts.add(new Post());
			top3_posts.add(new Post());

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public void traitement(){
		boolean lirepost=true;
		boolean lirecommentaire=true;
		lire(lirepost,lirecommentaire);
		do{
			// On sélectionne lequel du post ou du commentaire est le plus vieux
			//System.out.println("name :"+currentComment.getUser());
			//System.out.println("posttime"+currentPost.getTs());
			//System.out.println("commenttime"+currentComment.getTs());
			//System.out.println("time"+Date);
			if(postisolder()){
				lirepost=true;
				lirecommentaire=false;
				//On extrait la date
				Date=this.currentPost.getTs();
				//Si le post est le plus vieux, on le rajoute au tableau de Posts
				Post p = new Post();
				p.affecter(this.currentPost);
				this.posts.add(p);
				// ==> On reparcoure les tableaux de posts et comments pour mettre à jour les scores en fonction du temps écoulé (comparaison temps systeme/dates).
				this.updateScore();
			}else{
				lirepost=false;
				lirecommentaire=true;
				//On extrait la date
				Date=this.currentComment.getTs();

				//Si c'est un commentaire, on le rajoute dans le tableau des commentaires et on augmente le score de tous les commentaires et du post associés

				ajouter_commentaire();
				//On recalcule les scores
				this.updateScore();
			}

			/*System.out.println(d);
		System.out.println(posts);*/

			ArrayList<Post> listepost = chercher_top3();
			if(comparertop3(listepost)==false){
				Date da= new Date(Date);
				System.out.println(da);
				for(int i=0;i<top3_posts.size();i++){
					System.out.println(top3_posts.get(i));


				}
			}
			// A la fin on sort le top3 des scores
			// Si il est différent du précédent, on le change dans le fichier text de top3
		}while(lire(lirepost,lirecommentaire));
	}


	private ArrayList<Post> chercher_top3(){
		ArrayList<Post> listtop3 = new ArrayList<Post>();
		Post top1= new Post();
		Post top2= new Post();
		Post top3= new Post();
		//System.out.println("posts : "+posts);
		//System.out.println("top3 = "+top3);
		for(int i=0;i<this.posts.size();i++){
			if(posts.get(i).getPost_score()>top1.getPost_score()){
				top1=posts.get(i);
			}else{
				if(posts.get(i).getPost_score()==top1.getPost_score()){
					if(posts.get(i).getTs()>top1.getTs()){
						top1=posts.get(i);
					}

					if(posts.get(i).getTs()==top1.getTs() ){
				if(	posts.get(i).getComments_associes().size()>top1.getComments_associes().size()){
						top1=posts.get(i);
				}

					}

				}
			}
		}
		for(int i=0;i<this.posts.size();i++){
			if(posts.get(i).getPost_score()>top2.getPost_score() && posts.get(i).getPost_id() != top1.getPost_id()){
				top2=posts.get(i);
			}else{
				if(posts.get(i).getPost_score()==top2.getPost_score()){
					if(posts.get(i).getTs()>top2.getTs() ){
						top2=posts.get(i);
					}
					if(posts.get(i).getTs()==top2.getTs() ){
				if(	posts.get(i).getComments_associes().size()>top2.getComments_associes().size()){
						top2=posts.get(i);
				}

					}

				}
			}
		}


		for(int i=0;i<this.posts.size();i++){
			if(posts.get(i).getPost_score()>top3.getPost_score() && posts.get(i).getPost_id() != top2.getPost_id() && posts.get(i).getPost_id() != top1.getPost_id()){
				top3=posts.get(i);
			}else{
				if(posts.get(i).getPost_score()==top3.getPost_score()){
					if(posts.get(i).getTs()>top3.getTs() ){
						top3=posts.get(i);
					}
					if(posts.get(i).getTs()==top3.getTs() ){
				if(	posts.get(i).getComments_associes().size()>top3.getComments_associes().size()){
						top3=posts.get(i);
				}

					}
				}
			}
		}


		listtop3.add(top1);
		listtop3.add(top2);
		listtop3.add(top3);
		//System.out.println(listtop3);

		return listtop3;
	}

	private boolean comparertop3(ArrayList<Post> liste){


		if(liste.get(0).getPost_id()==top3_posts.get(0).getPost_id() && liste.get(1).getPost_id()==this.top3_posts.get(1).getPost_id() && liste.get(2).getPost_id()==this.top3_posts.get(2).getPost_id()){
			return true;
		}else{

			top3_posts=liste;
			return false;
		}



	}






	private void ajouter_commentaire(){
		long commentreplied;
		int ligne = 0;
		long postreplied = 0;
		Comment com = new Comment();
		com.affecter(this.currentComment);
		this.comments.add(com);
		if(this.currentComment.getPost_commented()==0){

			commentreplied=this.currentComment.getComment_replied();
			// Cherche le commentaire avec cet ID et lui ajouter la ligne du tableau comments correspondante
			for(int i=0;i<comments.size();i++){
				if(comments.get(i).getComment_id()==commentreplied){
					comments.get(i).getComments_associes().add(comments.size()-1);
				}
			}
		}else{
			for(int i=0;i<posts.size();i++){
				postreplied=this.currentComment.getPost_commented();
				if(posts.get(i).getPost_id()==postreplied){

					posts.get(i).getComments_associes().add(comments.size()-1);
				}
			}

		}
	}

	private void  updateScore(){
		// Met à jour les scores des posts
		for(int i=0;i<posts.size();i++){
			this.posts.get(i).calculScore(Date);
		}


	}
	private boolean postisolder()
	{
		if(this.currentPost.getTs()==-1){
			return false;
		}
		if(this.currentComment.getTs()==-1){
			return true;
		}else{
			if(this.currentPost.getTs() < this.currentComment.getTs()){
				//Post plus vieux
				return true;
			}
			return false;
		}


	}


	private boolean lire(boolean lirepost,boolean lirecommentaire) {
		//System.out.println("lire");
		String line="";
		String linecomment = "";

		StringBuffer stringBuffer = new StringBuffer();
		try {
			if(lirepost){
				line = bufferedReaderpost.readLine();
				if(line != null){

					currentPost.loadFromString(line);

				}else{

					currentPost.setTs(-1);
					bool1=true;

				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if(lirecommentaire){
				linecomment= bufferedReadercomment.readLine();
				if(linecomment != null){
					//System.out.println(linecomment);
					currentComment.loadFromString(linecomment);
					//System.out.println("reste comment");

				}else{
					//System.out.println("plus de comment");
					currentComment.setTs(-1);
					bool2=true;
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(bool1==true && bool2==true){
			//System.out.println("fin");
			return false; // Vide donc on retourne false
		}
		return true;
	}





}

