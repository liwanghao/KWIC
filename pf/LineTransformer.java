package kwic.pf;

import java.io.CharArrayWriter;
import java.io.IOException;

public class LineTransformer extends Filter {

    public LineTransformer(Pipe input, Pipe output) {
        super(input, output);
    }

    @Override
    protected void transform() {
        try{

            // keeps the characters
            CharArrayWriter writer = new CharArrayWriter();

            int c = input_.read();
            while (c != -1) {

                // line has been read
                if (((char) c) == '\n') {

                    // current line
                    String line = writer.toString();

                    String[] words = line.split(" ");

                    words[0] = words[0].toUpperCase();
                    StringBuilder transform = new StringBuilder();
                    for (int i = 0; i < words.length; i++) {
                        transform.delete(0, transform.length());
                        transform.append(words[i]);
                        if (i < words.length - 1) {
                            transform.append(" ");
                        }else{
                            transform.append('\n');
                        }
                        // convert the transformed string into char array and write it to the output
                        char[] chars = transform.toString().toCharArray();
                        for (int j = 0; j < chars.length; j++)
                            output_.write(chars[j]);
                    }

                    // reset the character buffer
                    writer.reset();
                } else
                    writer.write(c);

                c = input_.read();
            }

            // close the pipe
            output_.closeWriter();
      }catch(IOException exc){
        exc.printStackTrace();
        System.err.println("KWIC Error: Could not make line transform.");
        System.exit(1);
      }

    }

}
