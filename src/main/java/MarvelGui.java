import netscape.javascript.JSException;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.*;
import java.lang.Character;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

/**
 * Created by NinjaHunter on 4/26/17.
 */

//TODO nextBatch and prevBatch buttons, list of comics appeared in for characters
public class MarvelGui extends JFrame{
    private JPanel rootPanel;
    private JTextField txtSearch;
    private JRadioButton rdoCharacter;
    private JRadioButton rdoComic;

    private JList lstResults;
    private JButton btnSearch;
    private JButton btnNextBatch;
    private JLabel imageLabel;
    private JEditorPane txtInfo2;


    DefaultListModel searchResultsModel;
    String selectedRdoName;
    protected ComicCharacter hero;
    protected Comic comic;
    LinkedList<LinkedList> resultList = new LinkedList<LinkedList>();
    String apiURL = "";

    protected MarvelGui() {
        setContentPane(rootPanel);
        setPreferredSize(new Dimension(800, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        txtInfo2.setContentType("text/html");

        configureRadioButtons();
        pack();


        searchResultsModel = new DefaultListModel();
        lstResults.setModel(searchResultsModel);

        configureButtons();

    }

    protected void configureButtons(){
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchResultsModel.clear();

                String name = txtSearch.getText();

                if(selectedRdoName.equalsIgnoreCase("characters?")) {
                    hero = new ComicCharacter(selectedRdoName);
                    hero.getKey();
                    String nameSearch = hero.nameSearch(name);
                    apiURL = hero.getFullUrl(nameSearch);
                    System.out.println(apiURL);

                }else if(selectedRdoName.equalsIgnoreCase("comics?")){
                    comic = new Comic(selectedRdoName);
                    comic.getKey();
                    String titleSearch = comic.titleSearch(name);
                    apiURL = comic.getFullUrl(titleSearch);
                    System.out.println(apiURL);

                }

                AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();
                asyncHttpClient.prepareGet(apiURL).execute(new AsyncCompletionHandler<Response>() {
                    @Override
                    public Response onCompleted(Response response) throws Exception {

                        //response.ResopnseBody() gets the Json data.
                        //Variable responseBody has the Json data.
                        String responseBody = response.getResponseBody();
                        System.out.println(responseBody);
                        LinkedList info = new LinkedList();
                        try {
                            if(resultList.size() > 0){
                                resultList.clear();
                            }
                            for(int x = 0 ; x <= 5 ; x++) {
                                if(selectedRdoName.equalsIgnoreCase("characters?")) {
                                    info = hero.getInfo(responseBody, x);
                                }else if(selectedRdoName.equalsIgnoreCase("comics?")) {
                                    info = comic.getInfo(responseBody, x);
                                }
                                searchResultsModel.addElement(info.get(0).toString());
                                resultList.add(info);
                            }

                            if(resultList.size() <= 0){
                                JOptionPane.showMessageDialog(MarvelGui.this,"No results");
                            }
                        }catch(JSException e){
                            //TODO better exception handling
                            e.printStackTrace();
                        }

                        return null;
                    }
                });

            }
        });


        lstResults.addListSelectionListener(new ListSelectionListener() {
            //replaces get description button
            public void valueChanged(ListSelectionEvent e) {
                int index = 0;

                if(lstResults.isSelectionEmpty() == true){
                    return;
                }else {
                    String image = null;
                    index = lstResults.getSelectedIndex();
                    LinkedList descrip = resultList.get(index);
                    //String[] split = descrip.get(1).toString().split(",");
                    String bio = descrip.get(1).toString();

                    txtInfo2.setText( "<p><b>" + descrip.get(0).toString() + "</b></p>" + bio);

                    if(selectedRdoName.equalsIgnoreCase("characters?")) {
                        image = hero.getImage(descrip.get(2).toString());
                    }else if (selectedRdoName.equalsIgnoreCase("comics?")){
                        image = comic.getImage(descrip.get(2).toString());
                    }

                    ImageIcon icon = new ImageIcon(image); // This fails silently if file doesn't exist. No image shown.
                    imageLabel.setText("");
                    icon.getImage().flush();   // Any previous image is cached, so need to get rid of if
                    imageLabel.setIcon(icon);
                    pack();


                    //System.out.println(url);
                }

            }
        });
    }

    protected void configureRadioButtons(){
        ItemListener listener = new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                JRadioButton r = (JRadioButton)e.getSource();
                if(r.isSelected()) {
                    if(r.getText().equals(rdoCharacter.getText())){
                        rdoComic.setSelected(false);
                    }else if(r.getText().equals(rdoComic.getText())){
                        rdoCharacter.setSelected(false);
                    }
                    selectedRdoName = r.getText().toLowerCase() + "?";
                }
            }
        };
        rdoCharacter.addItemListener(listener);
        rdoComic.addItemListener(listener);

    }



}