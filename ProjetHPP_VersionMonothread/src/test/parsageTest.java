package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import main.Comment;
import main.Post;

public class parsageTest {

    private FileReader filepost;
    private FileReader filecomment;
    public BufferedReader bufferedReaderpost;
    public BufferedReader bufferedReadercomment;

    public Post currentPost;
    public Comment currentComment;

    public parsageTest() {
        try {
            filepost= new FileReader("/users/jeredentan/Desktop/posts.txt");
            bufferedReaderpost   = new BufferedReader(filepost);
            filecomment= new FileReader("/users/jeredentan/Desktop/comments.txt");
            bufferedReadercomment   = new BufferedReader(filecomment);

            currentPost = new Post();
            currentComment = new Comment();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }

    public static void main(String[] args) throws IOException {
        parsageTest t = new parsageTest();
        String line ="";
        long nbPost = 0;
        long nbCom = 0;
        long t1 = System.currentTimeMillis();
        while((line = t.bufferedReaderpost.readLine()) != null){
            t.currentPost.loadFromString(line);
            nbPost++;
        }
        long t3 = System.currentTimeMillis();
        System.out.println("Fin posts : "+(t3-t1));
        System.out.println("nbligne:"+nbPost);

        while((line = t.bufferedReadercomment.readLine()) != null){
            t.currentComment.loadFromString(line);
            nbCom++;
        }
        long t2 = System.currentTimeMillis();
        System.out.println("temps:"+(t2-t1));
        System.out.println("nbligne:"+nbCom);



    }
}
