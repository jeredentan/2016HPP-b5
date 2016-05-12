package versionperformante;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Ordonnanceur {
	//public static ArrayList<Post> posts = new ArrayList<Post>();
	public static HashMap<Long,Post> posts = new HashMap<Long,Post>();
	public static HashMap<Long,Comment> comments = new HashMap<Long,Comment>();
	public static ArrayList<Post> top3_posts= new ArrayList<Post>();
	public static ArrayList<Post> postsbis = new ArrayList<Post>();
	private long Date;
	private Post currentPost;
	private Comment currentComment;
	private FileReader filepost;
	private FileReader filecomment;
	private BufferedReader bufferedReaderpost;
	private BufferedReader bufferedReadercomment;
	private Boolean bool1=false;
	private Boolean bool2=false;
	private long nbcomm;


	public Ordonnanceur(){
		nbcomm=0;
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
		ArrayList<Post> listepost= new ArrayList<Post>();
		long t1= System.currentTimeMillis();
		boolean lirepost=true;
		boolean lirecommentaire=true;
		lire(lirepost,lirecommentaire);
		do{
			// On sélectionne lequel du post ou du commentaire est le plus vieu
			if(postisolder()){
				lirepost=true;
				lirecommentaire=false;
				//On extrait la date
				Date=this.currentPost.getTs();
				//Si le post est le plus vieux, on le rajoute au tableau de Posts
				Post p = new Post();
				p.affecter(this.currentPost);
				this.postsbis.add(p);
				this.posts.put(p.getPost_id(),p);
				//this.posts.add(p);
				// ==> On reparcoure les tableaux de posts et comments pour mettre à jour les scores en fonction du temps écoulé (comparaison temps systeme/dates).
				listepost=this.updateScore();
			}else{
				lirepost=false;
				lirecommentaire=true;
				//On extrait la date
				Date=this.currentComment.getTs();

				//Si c'est un commentaire, on le rajoute dans le tableau des commentaires et on augmente le score de tous les commentaires et du post associés

				ajouter_commentaire();
				//On recalcule les scores
				listepost=	this.updateScore();
			}

			/*System.out.println(d);
		System.out.println(posts);*/


			if(comparertop3(listepost)==false){
				/*Date da= new Date(Date);
				System.out.println(da);
				for(int i=0;i<top3_posts.size();i++){
					System.out.println(top3_posts.get(i));



				}*/

			}

			if(nbcomm % 10000==0){
				long t2= System.currentTimeMillis();
				System.out.println(nbcomm+ "commentaires");
				System.out.println(t2-t1);
			}
			nbcomm=nbcomm+1;
			// A la fin on sort le top3 des scores
			// Si il est différent du précédent, on le change dans le fichier text de top3
		}while(lire(lirepost,lirecommentaire));
		/*Date da= new Date(Date);
		System.out.println(da);
		for(int i=0;i<top3_posts.size();i++){
		System.out.println(top3_posts.get(i));


		}*/
	}

	/*
	private ArrayList<Post> chercher_top3(){
		ArrayList<Post> listtop3 = new ArrayList<Post>();
		Post top1= new Post();
		Post top2= new Post();
		Post top3= new Post();
		//System.out.println("posts : "+posts);
		//System.out.println("top3 = "+top3);

		for (HashMap.Entry<Long, Post> entry : posts.entrySet()){


			if(post.getPost_score()>top1.getPost_score()){
				top1=entry.getValue();
			}else{
				if(entry.getValue().getPost_score()==top1.getPost_score()){
					if(entry.getValue().getTs()>top1.getTs()){
						top1=entry.getValue();
					}

					if(entry.getValue().getTs()==top1.getTs() ){
						if(	entry.getValue().getComments_associes().size()>top1.getComments_associes().size()){
							top1=entry.getValue();
						}

					}

				}
			}
		}
		for (HashMap.Entry<Long, Post> entry : posts.entrySet()){


			if(entry.getValue().getPost_score()>top2.getPost_score() && entry.getValue().getPost_id()!=top1.getPost_id()){
				top2=entry.getValue();
			}else{
				if(entry.getValue().getPost_score()==top2.getPost_score()  && entry.getValue().getPost_id()!=top1.getPost_id()){
					if(entry.getValue().getTs()>top1.getTs()){
						top2=entry.getValue();
					}

					if(entry.getValue().getTs()==top2.getTs() ){
						if(	entry.getValue().getComments_associes().size()>top2.getComments_associes().size() && entry.getValue().getPost_id()!=top1.getPost_id()){
							top2=entry.getValue();
						}

					}

				}
			}
		}
		for (HashMap.Entry<Long, Post> entry : posts.entrySet()){


			if(entry.getValue().getPost_score()>top3.getPost_score() && entry.getValue().getPost_id()!=top1.getPost_id() && entry.getValue().getPost_id()!=top2.getPost_id()){
				top3=entry.getValue();
			}else{
				if(entry.getValue().getPost_score()==top3.getPost_score()  && entry.getValue().getPost_id()!=top1.getPost_id() && entry.getValue().getPost_id()!=top2.getPost_id()){
					if(entry.getValue().getTs()>top1.getTs()){
						top3=entry.getValue();
					}

					if(entry.getValue().getTs()==top3.getTs() ){
						if(	entry.getValue().getComments_associes().size()>top3.getComments_associes().size() && entry.getValue().getPost_id()!=top1.getPost_id() && entry.getValue().getPost_id()!=top2.getPost_id()){
							top3=entry.getValue();
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
	}*/



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
		this.comments.put(com.getComment_id(), com);

		if(this.currentComment.getPost_commented()==0){

			commentreplied=this.currentComment.getComment_replied();
			// Cherche le commentaire avec cet ID et lui ajouter la ligne du tableau comments correspondante

			comments.get(commentreplied).getComments_associes().add(currentComment.getComment_id());

		}else{
			for (HashMap.Entry<Long, Post> entry : posts.entrySet()){
				postreplied=this.currentComment.getPost_commented();
				if(entry.getValue().getPost_id()==postreplied){
					entry.getValue().getComments_associes().add(currentComment.getComment_id());
				}
			}

		}
	}

	public void update_top3(ArrayList<Post> top3_post,Post post,int score){

		if(score>top3_post.get(0).getPost_score()){
			top3_post.add(0, post);
			top3_post.remove(3);
		}else{
			if(score==top3_post.get(0).getPost_score()){
				if(post.getTs()>top3_post.get(0).getTs()){
					top3_post.add(0, post);
					top3_post.remove(3);
				}

				if(post.getTs()==top3_post.get(0).getTs() ){
					if(	post.getComments_associes().size()>top3_post.get(0).getComments_associes().size()){
						top3_post.add(0, post);
						top3_post.remove(3);
					}

				}

			}else{
				if(score>top3_post.get(1).getPost_score()){
					top3_post.add(1, post);
					top3_post.remove(3);
				}else{
					if(score==top3_post.get(0).getPost_score()){
						if(post.getTs()>top3_post.get(1).getTs()){
							top3_post.add(1, post);
							top3_post.remove(3);
						}

						if(post.getTs()==top3_post.get(1).getTs() ){
							if(	post.getComments_associes().size()>top3_post.get(1).getComments_associes().size()){
								top3_post.add(1, post);
								top3_post.remove(3);
							}

						}

					}else{
						if(score>top3_post.get(2).getPost_score()){
							top3_post.add(2, post);
							top3_post.remove(3);
						}else{
							if(score==top3_post.get(0).getPost_score()){
								if(post.getTs()>top3_post.get(2).getTs()){
									top3_post.add(2, post);
									top3_post.remove(3);
								}

								if(post.getTs()==top3_post.get(2).getTs() ){
									if(	post.getComments_associes().size()>top3_post.get(2).getComments_associes().size()){
										top3_post.add(2, post);
										top3_post.remove(3);
									}

								}

							}
						}

					}
				}
			}
		}

	}




	public ArrayList<Post> updateScore(){
		// Met à jour les scores des posts
		int score;
		ArrayList<Post> top3_post = new ArrayList<Post>();
		top3_post.add(new Post());
		top3_post.add(new Post());
		top3_post.add(new Post());
		Post p= new Post();
		for (int i=0;i<postsbis.size();i++)
		{

			p=postsbis.get(i);
			score=  p.calculScore(Date);
			if(score==0){
				postsbis.remove(p);
			}

			update_top3(top3_post,p,score);
		}

		return top3_post;

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

