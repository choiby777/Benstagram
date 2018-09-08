package com.cby.benstagram.Util;

public class StringManipulation {

    public static String expandUsername(String username){
        return username.replace("."," ");
    }

    public static String condenseUsername(String username){
        return username.replace(" " , ".");
    }

    /**
     * Input : #tag1 #tag2 #tag3...
     * Output : #tag1,#tag2,#tag3...
     *
     * @param tagFullString
     * @return
     */
    public static String getTags(String tagFullString) {

        if (tagFullString.indexOf("#") > 0){

            StringBuilder sb = new StringBuilder();
            char[] charArray = tagFullString.toCharArray();

            boolean foundMatch = false;
            for (char c : charArray){
                if (c == '#'){
                    foundMatch = true;
                    sb.append(c);
                }else{
                    if (foundMatch){
                        sb.append(c);
                    }
                }

                if (c == ' '){
                    foundMatch = false;
                }
            }

            String s = sb.toString().replace(" ", "").replace("#", " #");
            return s.substring(1, s.length());
        }

        return tagFullString;
    }
}
