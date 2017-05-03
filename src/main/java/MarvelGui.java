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
public class MarvelGui extends JFrame{
    private JPanel rootPanel;
    private JTextField txtSearch;
    private JRadioButton rdoCharacter;
    private JRadioButton rdoComic;
    private JRadioButton rdoEvent;
    private JList lstResults;
    private JButton btnSearch;
    private JButton btnGetDescription;
    private JTextArea txtInfo;

    DefaultListModel searchResultsModel;
    String selectedRdoName;
    protected ComicCharacter hero;
    LinkedList<LinkedList> resultList = new LinkedList<LinkedList>();


    protected MarvelGui() {
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        configureRadioButtons();


        searchResultsModel = new DefaultListModel();
        lstResults.setModel(searchResultsModel);

        configureButtons();

    }

    protected void configureButtons(){
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String name = txtSearch.getText();
                hero = new ComicCharacter(selectedRdoName);
                hero.getKey();
                String heroURL = hero.getFullUrl(name);
                System.out.println(heroURL);

                AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();
                asyncHttpClient.prepareGet(heroURL).execute(new AsyncCompletionHandler<Response>() {
                    @Override
                    public Response onCompleted(Response response) throws Exception {

                        //response.ResopnseBody() gets the Json data.
                        //Variable responseBody has the Json data.
                        String responseBody = response.getResponseBody();
                        System.out.println(responseBody);
                        try {
                            for(int x = 0 ; x <= 5 ; x++) {
                                LinkedList heroInfo = hero.getInfo(responseBody, x);
                                searchResultsModel.addElement(heroInfo.get(0).toString());
                                resultList.add(heroInfo);
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
        btnGetDescription.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = lstResults.getSelectedIndex();
                LinkedList descrip = resultList.get(index);
                txtInfo.setText(descrip.get(0).toString() + "\n" + descrip.get(1).toString());
            }
        });

        lstResults.addListSelectionListener(new ListSelectionListener() {
            //replaces get description button
            public void valueChanged(ListSelectionEvent e) {
                int index = lstResults.getSelectedIndex();
                LinkedList descrip = resultList.get(index);
                //String[] split = descrip.get(1).toString().split(",");
                txtInfo.setText(descrip.get(0).toString() + "\n" + descrip.get(1).toString());

            }
        });
    }

    protected void configureRadioButtons(){
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JRadioButton r = (JRadioButton)e.getSource();
                if(r.isSelected()) {
                    selectedRdoName = r.getText().toLowerCase() + "?";
                }
            }
        };
        rdoCharacter.addActionListener(listener);
        rdoComic.addActionListener(listener);
        rdoEvent.addActionListener(listener);

    }

}