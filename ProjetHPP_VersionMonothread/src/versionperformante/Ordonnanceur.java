package versionperformante;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
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

	private FileWriter filesortie;
	private BufferedWriter bufferedSortie;
	private Boolean bool1=false;
	private Boolean bool2=false;
	private long nbcomm;

	private long pourcent;


	public Ordonnanceur(){
		nbcomm=0;
		currentPost= new Post();
		currentComment= new Comment();
		try {
			//filepost= new FileReader("/Users/Jeredentan/Desktop/posts.txt");
			//filepost= new FileReader("D:/Documents/data/posts.dat");
			filepost= new FileReader("D:/Temp/data/posts.dat");
			bufferedReaderpost   = new BufferedReader(filepost);
			//filecomment= new FileReader("/Users/Jeredentan/Desktop/comments.txt");
			//filecomment= new FileReader("D:/Documents/data/comments.dat");
			filecomment= new FileReader("D:/Temp/data/comments.dat");
			bufferedReadercomment   = new BufferedReader(filecomment);

			//filesortie = new FileWriter("/Users/Jeredentan/Desktop/sortie.txt");
			//filesortie = new FileWriter("D:/Documents/data/sortie.txt");
			filesortie = new FileWriter("D:/Temp/data/sortie.txt");
			bufferedSortie = new BufferedWriter(filesortie);
			top3_posts.add(new Post());
			top3_posts.add(new Post());
			top3_posts.add(new Post());
			nbcomm =1;
			pourcent =1;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Methode faissant le traitement des donnees
	 */
	public void traitement(){
		boolean changetop3=false;
		long t1= System.currentTimeMillis();
		boolean lirepost=true;
		boolean lirecommentaire=true;
		lire(lirepost,lirecommentaire);
		do{
			// On sÃ©lectionne lequel du post ou du commentaire est le plus vieu
			if(postisolder()){
				lirepost=true;
				lirecommentaire=false;
				//On extrait la date
				Date=this.currentPost.getTs();
				//Si le post est le plus vieux, on le rajoute au tableau de Posts
				Post p = new Post();
				p.affecter(this.currentPost);
				Ordonnanceur.postsbis.add(p);
			//	System.out.println("ajout d'un post a postbis");
				Ordonnanceur.posts.put(p.getPost_id(),p);
				// ==> On reparcoure les tableaux de posts et comments pour mettre Ã  jour les scores en fonction du temps Ã©coulÃ© (comparaison temps systeme/dates).
				changetop3=this.updateScore(true,p);
				if(posts.size() >= 55){
					//	System.out.println("debug");
				}
			}else{
				lirepost=false;
				lirecommentaire=true;
				//On extrait la date
				Date=this.currentComment.getTs();
				//Si c'est un commentaire, on le rajoute dans le tableau des commentaires et on augmente le score de tous les commentaires et du post associÃ©s
				ajouter_commentaire();
				//On recalcule les scores
				changetop3=	this.updateScore(false,currentPost);
			}
			/*System.out.println(d);
			System.out.println(posts);*/
			if(changetop3==true){
				java.util.Date da= new java.util.Date(Date);

				try {
					bufferedSortie.write("\n"+da.toString()+"\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for(int i=0;i<top3_posts.size();i++){
				try {
					bufferedSortie.write(top3_posts.get(i).toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			}

			if(nbcomm % 10000==0){
				long t2= System.currentTimeMillis();
				System.out.println(nbcomm+ "commentaires");
				System.out.println(t2-t1);
				//System.out.println("pourcent de post calculer :" + (100*pourcent/nbcomm));
				//System.out.println("taille postsbis "+postsbis.size());
				System.out.println("debit (ligne/s): "+nbcomm*1000d/(double)(t2-t1));
			}
			nbcomm=nbcomm+1;
			// A la fin on sort le top3 des scores
			// Si il est diffÃ©rent du prÃ©cÃ©dent, on le change dans le fichier text de top3
		}while(lire(lirepost,lirecommentaire));


		System.out.println("fini");
	}

	/**
	 * Permet d'ajouter le commentaire dans current comment et le place de fa�on adequate dans les differents tableau
	 */
	private void ajouter_commentaire(){
		long commentreplied;

		long postreplied = 0;
		Comment com = new Comment();
		com.affecter(this.currentComment);
		this.comments.put(com.getComment_id(), com);

		if(this.currentComment.getPost_commented()==0){

			commentreplied=this.currentComment.getComment_replied();
			// Cherche le commentaire avec cet ID et lui ajouter la ligne du tableau comments correspondante

			comments.get(commentreplied).getComments_associes().add(currentComment.getComment_id());

		}else{

			postreplied=this.currentComment.getPost_commented();

			posts.get(postreplied).getComments_associes().add(currentComment.getComment_id());
			posts.get(postreplied).setLastupdate(Date);
			posts.get(postreplied).incrementnbComs();

			// Recherche du post dans post bis pour le mettre en tete de postbis
			for(int i =0; i<postsbis.size();i++){
				if(postsbis.get(i).getPost_id() == postreplied){
					//Post p = new Post();
					//p = posts.get(postreplied);
					//p.setLastupdate(Date);
					postsbis.remove(i);
					postsbis.add(0, posts.get(postreplied));
				}
			}


		}
	}

	/**
	 * @param top3_post
	 * @param post
	 * @param score
	 * @return si le top trois a change return true, false sinon
	 */
	public boolean update_top3(ArrayList<Post> top3_post,Post post,int score){
		boolean change = false;
		if(score>top3_post.get(0).getPost_score()){
			top3_post.add(0, post);
			top3_post.remove(3);
			change = true;

		}else{
			if(score==top3_post.get(0).getPost_score()){
				if(post.getTs()>top3_post.get(0).getTs()){
					top3_post.add(0, post);
					top3_post.remove(3);
					change = true;
				}

				if((post.getTs()==top3_post.get(0).getTs() )){
					if((	post.getComments_associes().size()>top3_post.get(0).getComments_associes().size())){
						top3_post.add(0, post);
						top3_post.remove(3);
						change = true;
					}

				}

			}else{
				if(score>top3_post.get(1).getPost_score()){
					top3_post.add(1, post);
					top3_post.remove(3);
					change = true;
				}else{
					if(score==top3_post.get(1).getPost_score()){
						if(post.getTs()>top3_post.get(1).getTs()){
							top3_post.add(1, post);
							top3_post.remove(3);
							change = true;
						}

						if(post.getTs()==top3_post.get(1).getTs() ){
							if((post.getComments_associes().size()>top3_post.get(1).getComments_associes().size())&&(post.getPost_id()==top3_post.get(0).getPost_id())){
								top3_post.add(1, post);
								top3_post.remove(3);
								change = true;
							}

						}

					}else{
						if(score>top3_post.get(2).getPost_score()){
							top3_post.add(2, post);
							top3_post.remove(3);
							change = true;
						}else{
							if(score==top3_post.get(2).getPost_score()){
								if(post.getTs()>top3_post.get(2).getTs()){
									top3_post.add(2, post);
									top3_post.remove(3);
									change = true;
								}

								if(post.getTs()==top3_post.get(2).getTs() ){
									if((post.getComments_associes().size()>top3_post.get(2).getComments_associes().size())&&(post.getPost_id()==top3_post.get(1).getPost_id())&&(post.getPost_id()==top3_post.get(0).getPost_id())){
										top3_post.add(2, post);
										top3_post.remove(3);
										change = true;
									}

								}

							}
						}

					}
				}
			}
		}
		return change;

	}

	/**
	 * Recalcul et place dans le bon ordre le top 3
	 * @return true si il a change false sinon
	 */
	public boolean majtop3(){
		boolean changed = false;
		int scoretop3=0;
		for(int i=0;i<3;i++){
			scoretop3=top3_posts.get(i).calculScore(Date);
			if(scoretop3==0){
				changed =true;
			}
		}
		for(int j = 0; j<2;j++){
			for(int i =0; i <2;i++){
				if(top3_posts.get(i).getPost_score() < top3_posts.get(i+1).getPost_score()){
					Post p = new Post();
					Post p2 = new Post();
					p = top3_posts.get(i);
					top3_posts.add(i,  top3_posts.get(i+1));
					top3_posts.remove(i+1);
					top3_posts.add(i+1,p);
					top3_posts.remove(i+2);
					changed = true;
				}
			}
		}
		return changed;
	}
	
	/**
	 * methode mettant a jour les score et actualisant le top 3
	 * @param ispost si la derni�re update est un post 
	 * @param post le dernier post
	 * @return true si le top 3 a changer false sinon
	 */
	public boolean updateScore(boolean ispost,Post post){
		boolean change = false;
		boolean aux = false;
		int scoretop3;
		long t;
		int score;


		//1- Mise a� jour du top 3 pour virer les obsoletes
		if(majtop3()){
			change = true;
		}
		//2 - Cacul du score pour post/commentaire(faire en sorte de pas calculer tt le tps)
		if(ispost){
			score=currentPost.calculScore(Date);
			aux = update_top3(top3_posts,post,score);
			if(aux){change = true;}
		}else{
			Post p = new Post();
			long id;
			Comment comment = new Comment();
			comment=currentComment;
			t=comment.getPost_commented();
			while(t==0){
				id=comment.getComment_replied();
				comment=comments.get(id);
				t=comment.getPost_commented();

			}
			
			p = posts.get(t);
			score = p.calculScore(Date);
			
			for(int i=0;i<3;i++){
				scoretop3=top3_posts.get(i).calculScore(Date);
				if(scoretop3==0){
					change =true;
				}
			}
			
			aux=update_top3(top3_posts,p,score);
			if(aux){change = true;}
		}







		//3 - On calcul les scores des postes qui pouraient rentré dans le top 3 (ie nbcom*10 > 3iem score)

		completertop3(change);

		return change;

	}


	/**
	 * Methode servant a chercher dans les posts les candidats a rejoindre le top3 et le cas echeant les y ajouters
	 */
	private void completertop3(boolean change){
		int score3iem = top3_posts.get(2).getPost_score();
		boolean aux = false;
		Post post = new Post();
		for(int i=0;i<postsbis.size();i++){
			post = postsbis.get(i);
			if((Date-post.getLastupdate())/86_400_000 < 10){ //post actif

				if(post.getnbComs()*10>=score3iem){
					post.calculScore(Date);
					aux = update_top3(top3_posts, post, post.getPost_score());
						if(aux){change = true;}
					score3iem = top3_posts.get(2).getPost_score();
					pourcent++;
				}
			}else{

				postsbis.remove(i);

			}


		}
	}

	/**
	 * Compare le post dans current post et le commentaire dans current comment pour savoir lequel est le plus vieux
	 * @return
	 */
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

	/**
	 * lit un commentaire et ou un fichier depuis les fichiers
	 * @param lirepost	true si on veux lire un post false sinon
	 * @param lirecommentaire true si on veux lire un commentaire false sinon
	 * @return true si il y a des choses a lire false si les dossiers sont vides
	 */
	private boolean lire(boolean lirepost,boolean lirecommentaire) {
		//System.out.println("lire");
		String line="";
		String linecomment = "";


		StringBuffer stringBuffer = new StringBuffer();
		try {
			if(lirepost){
				line = bufferedReaderpost.readLine();
				if(line != null){
					currentPost = new Post();
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