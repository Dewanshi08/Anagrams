/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private int wordLength = DEFAULT_WORD_LENGTH;
    private Random random = new Random();

    HashMap<String,ArrayList<String>> lettersToWord= new HashMap<String,ArrayList<String>>();
    HashSet<String> wordSet= new HashSet<>();
    ArrayList<String> wordList= new ArrayList<>();
    HashMap<Integer,ArrayList<String>> sizeToWords= new HashMap<Integer,ArrayList<String>>();

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);

            wordSet.add(word);

            String key= sortLetters(word);
            if(!lettersToWord.containsKey(key))
            {
                ArrayList<String> value= new ArrayList<>();
                value.add(word);
                lettersToWord.put(key,value);
            }
            else
            {
                ArrayList<String> value= lettersToWord.get(key);
                value.add(word);
                lettersToWord.put(key,value);
            }

            int len= word.length();
            if(!sizeToWords.containsKey(len))
            {
                ArrayList<String> value= new ArrayList<>();
                value.add(word);
                sizeToWords.put(len,value);
            }
            else
            {
                ArrayList<String> value= sizeToWords.get(len);
                value.add(word);
                sizeToWords.put(len,value);
            }
        }
    }

    public String sortLetters(String temp)
    {
        char c[] = temp.toCharArray();
        Arrays.sort(c);
        String sorted = new String(c);
        return sorted;
    }

    public boolean isGoodWord(String word, String base)
    {
        if(wordSet.contains(word) && !(word.contains(base)))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String st = sortLetters(targetWord);
        result=lettersToWord.get(st);
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(char i='a';i<='z';i++)
        {
            String new_word= sortLetters(word+i);
            if(lettersToWord.containsKey(new_word))
            {
                ArrayList<String> temp_arr= lettersToWord.get(new_word);
                for(String j:temp_arr)
                {
                    if(isGoodWord(j,word))
                    {
                        result.add(j);
                    }
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        ArrayList<String> list = sizeToWords.get(wordLength);
        int index = random.nextInt(list.size());
        int i=index;
        String temp = list.get(index);
        String temp1 = sortLetters(temp);
        while(lettersToWord.get(temp1).size()!=MIN_NUM_ANAGRAMS)
        {
            index++;
            index=index%list.size();
            if(index==i){

                break;
            }
            temp=list.get(index);
            temp1=sortLetters(temp);
        }
        if((wordLength+1)<=(MAX_WORD_LENGTH))
        {
            wordLength++;
        }
        return temp;
    }
}
