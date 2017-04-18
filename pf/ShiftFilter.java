package kwic.pf;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;


public class ShiftFilter extends Filter {
    private InputStream noisein_;
    private ArrayList<String> noiseWords;
    private ArrayList<String> lines;
    public ShiftFilter(Pipe input, Pipe output, InputStream noisein) {
        super(input, output);
        noisein_=noisein;
        noiseWords= new ArrayList<>();
        lines= new ArrayList<>();
    }

    private void readNoiseWord(){

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(noisein_))){
            String word = reader.readLine();
            word=word.replace("\n", "");
            noiseWords.add(word);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Override
    protected void transform() {
        try{

            //first read noise words
            readNoiseWord();
            CharArrayWriter writer = new CharArrayWriter();
            int c= input_.read();
            while(c != -1){
                writer.write(c);

                    // add line
                if(((char) c) == '\n'){
                  String line = writer.toString();
                  String[] words = line.split(" ");
                  boolean hasNoise=false;
                  for(String word:noiseWords){
                      if(word.equals(words[0])){
                          hasNoise=true;
                      }
                  }
                  if(!hasNoise){
                      lines.add(line);
                  }
                  writer.reset();
                }

                c = input_.read();
              }

            //write  lines to the output pipe
        Iterator iterator = lines.iterator();
        while(iterator.hasNext()){
          char[] chars = ((String) iterator.next()).toCharArray();
          for(int i = 0; i < chars.length; i++)
            output_.write(chars[i]);
        }

            // close the pipe
        output_.closeWriter();
      }catch(IOException exc){
        exc.printStackTrace();
        System.err.println("KWIC Error: Could not delete noise shifts.");
        System.exit(1);
      }
    }

}
