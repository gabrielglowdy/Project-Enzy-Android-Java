package id.nganggur.projectenzy;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;


public class Brain {
    ArrayList<ObjectInput> knowledges;
    Context mContext;
    String separator = "->";
    private String listName = "datas";
    private String TAG = "DebugMode";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private String debugCode = "debugMode", langCode = "langCode";
    String prevQuery = "";

    boolean debugMode;
    boolean learnMode = false;
    boolean bahasa;

    public boolean isBahasa() {
        return bahasa;
    }

    public void setBahasa(boolean bahasa) {
        this.bahasa = bahasa;
        String languageToLoad;
        if (bahasa){
            languageToLoad = "ID"; // your language
        }else{
            languageToLoad = "en";
        }
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        mContext.getResources().updateConfiguration(config,
                mContext.getResources().getDisplayMetrics());
        editor.putBoolean(langCode,this.bahasa);
        editor.commit();
    }

    public Brain(Context context) {
        mContext = context;
        knowledges = new ArrayList<>();
        prefs = mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);
        editor = prefs.edit();

        this.debugMode = prefs.getBoolean(debugCode,false);
        this.bahasa = prefs.getBoolean(langCode,false);

        if (debugMode) {
            Log.d(TAG, "Debug Mode : " + debugMode);
        }
        readFile();
        commonKnowledge();
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
        Log.d(TAG, "setDebugMode: " + debugMode);
        editor.putBoolean(debugCode,this.debugMode);
        editor.commit();
    }

    public String toggleDebug() {
        setDebugMode(!isDebugMode());
        return mContext.getString(R.string.toogledebug) + this.debugMode;
    }

    public void writeFile() {
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < knowledges.size(); i++) {
            temp.add(knowledges.get(i).getQuery() + separator + knowledges.get(i).getResult());
        }
        Set<String> set = new HashSet<>();
        set.addAll(temp);
        editor.putStringSet(listName, set);
        editor.commit();

        if (debugMode) {
            Log.d(TAG, "writeFile: Success");
        }
    }



    public void readFile() {
        ArrayList<String> temp = new ArrayList<>();
        Set<String> set = prefs.getStringSet(listName, null);
        if (set!=null){
            temp.addAll(set);
            knowledges.clear();
            for (int i = 0; i < temp.size() ; i++) {
                String tmp [] = temp.get(i).split(separator);
                addKnowledges(tmp[0],tmp[1]);
            }
        }
    }

    public void commonKnowledge() {
        learn("Hello", "Hi");
        learn("Hi", "HI there");
        learn(mContext.getString(R.string.name), "Enzy");
    }

    public String showKnowledges() {
        String res = "";
        for (int i = 0; i < knowledges.size(); i++) {
            res += (i + 1) + ". " + knowledges.get(i).getQuery() + "\t: " + knowledges.get(i).getResult() +" \n";
        }
        return res;
    }

    public String refresh() {
        knowledges.clear();
        readFile();
        return "refresh success";
    }

    public String find(String input) {

        if (learnMode==true && !prevQuery.equals("")){
            learnMode = false;
            boolean success = learn(prevQuery,input);
            String tmpQuery = prevQuery;
            prevQuery = "";
                return mContext.getString(R.string.i_know) + " '" + tmpQuery + "'";
        }
        ArrayList<MetaData> results = new ArrayList<>();
        String[] inputs = input.toLowerCase().split(" ");

        switch (input.toLowerCase()) {
            case "show()":
                this.prevQuery = "";
                return showKnowledges();

            case "about()":
                this.prevQuery = "";
                String about = mContext.getString(R.string.about);
                return about;

            case "toggledebug()":
                this.prevQuery = "";
                return toggleDebug();

            case "refresh()":
                this.prevQuery = "";
                return refresh();


            case "rearrange()":
                this.prevQuery = "";
                refresh();
                return rearrange();

            case "reset()":
                this.prevQuery = "";
                return reset();
        }
        for (int i = 0; i < knowledges.size(); i++) {
            if (knowledges.get(i).getQuery().toLowerCase().contains(input.toLowerCase())) {
                results.add(new MetaData(knowledges.get(i).getQuery(), knowledges.get(i).getResult()));
                break;
            }
        }

        if (inputs[0].equals(mContext.getString(R.string.are)) || inputs[0].equals("am") || inputs[0].equals("can") || inputs[0].equals("do")) {
            int angka = (int) Math.round(Math.random());
            if (angka == 0) {
                String yes = mContext.getString(R.string.yes);
                return yes;
            } else {
                String no = mContext.getString(R.string.answer_no);
                return no;
            }
        }

        String wrong = mContext.getString(R.string.wrong);
        String no = mContext.getString(R.string.no);
        if (input.toLowerCase().equals(wrong) || input.toLowerCase().equals(no) || input.toLowerCase().equals("no") || input.toLowerCase().equals("n")) {
            if (!prevQuery.equals("")) {
                return dontKnow(prevQuery);
            } else {
                String mean = mContext.getString(R.string.what_do_you_mean); 
                return mean + input + "?";
            }
        }

        for (int i = 0; i < knowledges.size(); i++) {
            for (int j = 0; j < inputs.length; j++) {
                if (inputs[j].length() > 2) {
                    for (int x = 2; x < inputs[j].length(); x++) {
                        String inputnya = inputs[j].toLowerCase().substring(0, x);
                        boolean skip = false;
                        if (inputs[j].toLowerCase().equals("what")) {
                            skip = true;
                        }
                        if (!skip && !inputnya.equals("is") && !inputnya.equals("are") && knowledges.get(i).getQuery().contains(inputnya)) {
                            boolean sameAnswer = false;
                            if (debugMode) {
                                Log.d(TAG, "found: " + inputnya);
                            }
                            for (int k = 0; k < results.size(); k++) {
                                if (results.get(k).getResult().equals(knowledges.get(i).getResult())) {
                                    results.get(k).upVote();
                                    sameAnswer = true;
                                    break;
                                }
                            }
                            if (!sameAnswer) {
                                results.add(new MetaData(knowledges.get(i).getQuery(), knowledges.get(i).getResult()));;
                            }
                        }
                    }
                }
            }
        }

        String res = "";
        int maxVote = 0;
        for (int i = 0; i < results.size(); i++) {
            if (i > 0 && results.get(i).getVote() > results.get(maxVote).getVote()) {
                maxVote = i;
            }
        }
        if (results.size()>0){
            res = results.get(maxVote).getResult();
        }
        if (debugMode && !res.equals("")) {
            //show all
            res += "\n=================\n";
            for (int i = 0; i < results.size(); i++) {
                res += results.get(i).getQuery() + " : " + results.get(i).getResult() + " (" + results.get(i).getVote() + " " + mContext.getString(R.string.votes) + ")\n";
            }
        }
        if (res.equals("")) {
            return dontKnow(input);
        }
            this.prevQuery = input;
        return res;
    }

    public String reset(){
        knowledges.clear();
        commonKnowledge();
        return mContext.getString(R.string.reset_knowledges);
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public boolean isLearnMode() {
        return learnMode;
    }

    public void setLearnMode(boolean learnMode) {
        this.learnMode = learnMode;
    }

    public boolean learn(String input, String output) {
        boolean success = addKnowledges(input, output);
        if (success) {
            writeFile();
        }
        return success;
    }

    public boolean addKnowledges(String input, String output) {
        boolean ketemu = false;
        for (int i = 0; i < knowledges.size(); i++) {
            if (knowledges.get(i).getQuery().toLowerCase().equals(input.toLowerCase())) {
                ketemu = true;
                knowledges.get(i).setResult(output);
                break;
            }
        }
        if (!ketemu) {
            knowledges.add(new ObjectInput(input.toLowerCase(), output));
            rearrange();
            return true;
        }
        return false;
    }

    public String dontKnow(String query) {
        learnMode = true;
        prevQuery = query;
        return mContext.getString(R.string.how_to_answer) + query + "' \n  " + mContext.getString(R.string.how_should);
    }

    public String rearrange() {
        boolean sorted = false;
        while (!sorted) {
            sorted = true;
            for (int i = 0; i < knowledges.size() - 1; i++) {
                if (knowledges.get(i).getQuery().length() > knowledges.get(i + 1).getQuery().length()) {
                    if (debugMode) {
                        Log.d(TAG, "rearrange: " + knowledges.get(i).getQuery() + separator + knowledges.get(i + 1).getQuery());
                    }
                    ObjectInput tmp = knowledges.get(i);
                    knowledges.set(i, knowledges.get(i + 1));
                    knowledges.set(i + 1, tmp);
                    sorted = false;
                }
            }
        }
        writeFile();
        return mContext.getString(R.string.rearrange);
    }
}
