package com.example.android.genericsocialnews;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class NewsUtil {
    private static final String data = "{\"response\":{\"status\":\"ok\",\"userTier\":\"developer\",\"total\":67063,\"startIndex\":1,\"pageSize\":10,\"currentPage\":1,\"pages\":6707,\"orderBy\":\"newest\",\"results\":[{\"id\":\"society/2018/jun/16/may-to-unveil-20-billion-pound-a-year-nhs-boost\",\"type\":\"article\",\"sectionId\":\"society\",\"sectionName\":\"Society\",\"webPublicationDate\":\"2018-06-16T21:30:08Z\",\"webTitle\":\"May to unveil £20bn a year boost to NHS spending\",\"webUrl\":\"https://www.theguardian.com/society/2018/jun/16/may-to-unveil-20-billion-pound-a-year-nhs-boost\",\"apiUrl\":\"https://content.guardianapis.com/society/2018/jun/16/may-to-unveil-20-billion-pound-a-year-nhs-boost\",\"fields\":{\"trailText\":\"Ministers to claim ‘Brexit health dividend’, but cabinet split over scale of tax increases\",\"wordcount\":\"986\",\"thumbnail\":\"https://media.guim.co.uk/f46e49926cf330eb1809a5b5099601c394bd3131/0_116_2532_1519/500.jpg\"},\"tags\":[{\"id\":\"profile/tobyhelm\",\"type\":\"contributor\",\"webTitle\":\"Toby Helm\",\"webUrl\":\"https://www.theguardian.com/profile/tobyhelm\",\"apiUrl\":\"https://content.guardianapis.com/profile/tobyhelm\",\"references\":[],\"bio\":\"<p>Toby Helm is the Observer's political editor</p>\",\"bylineImageUrl\":\"https://uploads.guim.co.uk/2017/06/05/Toby-Helm.jpg\",\"bylineLargeImageUrl\":\"https://uploads.guim.co.uk/2017/10/09/Toby_Helm,_L.png\",\"firstName\":\"helm\",\"lastName\":\"toby\"}],\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"society/2018/jun/16/mental-health-patient-out-of-area-nhs-care-treatment-jeremy-hunt\",\"type\":\"article\",\"sectionId\":\"society\",\"sectionName\":\"Society\",\"webPublicationDate\":\"2018-06-16T20:05:00Z\",\"webTitle\":\"Mental health patients still sent hundreds of miles for treatment\",\"webUrl\":\"https://www.theguardian.com/society/2018/jun/16/mental-health-patient-out-of-area-nhs-care-treatment-jeremy-hunt\",\"apiUrl\":\"https://content.guardianapis.com/society/2018/jun/16/mental-health-patient-out-of-area-nhs-care-treatment-jeremy-hunt\",\"fields\":{\"trailText\":\"Despite government promises to end practice, figures show almost no change since 2016\",\"wordcount\":\"705\",\"thumbnail\":\"https://media.guim.co.uk/e7aa178bbdd14549e4f061c74af7ba76525f30b2/0_192_5760_3456/500.jpg\"},\"tags\":[{\"id\":\"profile/deniscampbell\",\"type\":\"contributor\",\"webTitle\":\"Denis Campbell\",\"webUrl\":\"https://www.theguardian.com/profile/deniscampbell\",\"apiUrl\":\"https://content.guardianapis.com/profile/deniscampbell\",\"references\":[],\"bio\":\"<p>Denis Campbell is health policy editor for the Guardian and the Observer. He has written about the NHS, public health and medicine since 2007 and shares health-writing duties with Sarah Boseley, the health editor</p>\",\"bylineImageUrl\":\"https://uploads.guim.co.uk/2018/04/13/Denis-Campbell.jpg\",\"bylineLargeImageUrl\":\"https://uploads.guim.co.uk/2018/04/13/Denis_Campbell,_L.png\",\"firstName\":\"Denis\",\"lastName\":\"Campbell\"}],\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"society/2018/jun/16/billy-caldwells-mother-hopeful-of-cannabis-medicine-licence\",\"type\":\"article\",\"sectionId\":\"society\",\"sectionName\":\"Society\",\"webPublicationDate\":\"2018-06-16T12:02:00Z\",\"webTitle\":\"Home Office returns cannabis oil for boy's epilepsy treatment\",\"webUrl\":\"https://www.theguardian.com/society/2018/jun/16/billy-caldwells-mother-hopeful-of-cannabis-medicine-licence\",\"apiUrl\":\"https://content.guardianapis.com/society/2018/jun/16/billy-caldwells-mother-hopeful-of-cannabis-medicine-licence\",\"fields\":{\"trailText\":\"Sajid Javid uses special power to issue licence for Billy Caldwell to receive medicine<br>\",\"wordcount\":\"601\",\"thumbnail\":\"https://media.guim.co.uk/7277cde4af9426f4c5b8c0a64a413aef8b11e26b/0_44_3500_2100/500.jpg\"},\"tags\":[{\"id\":\"profile/mattha-busby\",\"type\":\"contributor\",\"webTitle\":\"Mattha Busby\",\"webUrl\":\"https://www.theguardian.com/profile/mattha-busby\",\"apiUrl\":\"https://content.guardianapis.com/profile/mattha-busby\",\"references\":[],\"bio\":\"<p>Mattha Busby is a freelance journalist. Twitter: @matthabusby<br></p>\",\"firstName\":\"Mattha\",\"lastName\":\"Busby\",\"twitterHandle\":\"matthabusby\"},{\"id\":\"profile/verity-bowman\",\"type\":\"contributor\",\"webTitle\":\"Verity Bowman\",\"webUrl\":\"https://www.theguardian.com/profile/verity-bowman\",\"apiUrl\":\"https://content.guardianapis.com/profile/verity-bowman\",\"references\":[],\"bio\":\"<p>Verity Bowman is a freelance reporter</p>\",\"firstName\":\"Verity\",\"lastName\":\"Bowman\"}],\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"society/2018/jun/16/share-your-thanks-to-the-nhs\",\"type\":\"article\",\"sectionId\":\"society\",\"sectionName\":\"Society\",\"webPublicationDate\":\"2018-06-16T09:00:41Z\",\"webTitle\":\"Share your thanks to the NHS\",\"webUrl\":\"https://www.theguardian.com/society/2018/jun/16/share-your-thanks-to-the-nhs\",\"apiUrl\":\"https://content.guardianapis.com/society/2018/jun/16/share-your-thanks-to-the-nhs\",\"fields\":{\"trailText\":\"We would like to hear what you are thankful for and what the service means to you ahead of its 70th anniversary<br>\",\"wordcount\":\"179\",\"thumbnail\":\"https://media.guim.co.uk/01032ca0748f341359dd83a872e6549770fba9de/0_342_5121_3074/500.jpg\"},\"tags\":[{\"id\":\"profile/guardian-readers\",\"type\":\"contributor\",\"webTitle\":\"Guardian readers\",\"webUrl\":\"https://www.theguardian.com/profile/guardian-readers\",\"apiUrl\":\"https://content.guardianapis.com/profile/guardian-readers\",\"references\":[],\"bio\":\"<p>The Guardian readers contributor tag is applied to any content that is solely or partly created by you, our readers. It includes projects, galleries and stories involving data, photography, perspectives and more. Thank you for your ongoing inspiration and participation</p>\",\"firstName\":\"readers\",\"lastName\":\"guardian\"}],\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"society/2018/jun/16/1bn-needed-to-stave-off-crisis-say-social-care-bosses\",\"type\":\"article\",\"sectionId\":\"society\",\"sectionName\":\"Society\",\"webPublicationDate\":\"2018-06-15T23:01:29Z\",\"webTitle\":\"£1bn needed to stave off crisis, say social care bosses\",\"webUrl\":\"https://www.theguardian.com/society/2018/jun/16/1bn-needed-to-stave-off-crisis-say-social-care-bosses\",\"apiUrl\":\"https://content.guardianapis.com/society/2018/jun/16/1bn-needed-to-stave-off-crisis-say-social-care-bosses\",\"fields\":{\"trailText\":\"The Association of Directors of Adult Services says lack of investment could leave older and disabled people struggling\",\"wordcount\":\"437\",\"thumbnail\":\"https://media.guim.co.uk/a2c111e845c6cee4634458525960eadcb9ca39a9/0_226_8688_5213/500.jpg\"},\"tags\":[{\"id\":\"profile/patrickbutler\",\"type\":\"contributor\",\"webTitle\":\"Patrick Butler\",\"webUrl\":\"https://www.theguardian.com/profile/patrickbutler\",\"apiUrl\":\"https://content.guardianapis.com/profile/patrickbutler\",\"references\":[],\"bio\":\"<p>Patrick Butler is editor of society, health and education policy for the <a href=\\\"http://www.guardian.co.uk/theguardian\\\">Guardian</a>. He was previously editor of <a href=\\\"http://www.guardian.co.uk/society\\\">SocietyGuardian.co.uk</a></p>\",\"bylineImageUrl\":\"https://uploads.guim.co.uk/2017/06/05/Patrick-Butler1.jpg\",\"bylineLargeImageUrl\":\"https://uploads.guim.co.uk/2017/10/09/Patrick_Butler,_L.png\",\"firstName\":\"butler\",\"lastName\":\"\"}],\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"society/2018/jun/15/number-of-children-waiting-six-months-for-dental-operations-soars\",\"type\":\"article\",\"sectionId\":\"society\",\"sectionName\":\"Society\",\"webPublicationDate\":\"2018-06-15T21:30:27Z\",\"webTitle\":\"Number of children waiting six months for dental operations soars\",\"webUrl\":\"https://www.theguardian.com/society/2018/jun/15/number-of-children-waiting-six-months-for-dental-operations-soars\",\"apiUrl\":\"https://content.guardianapis.com/society/2018/jun/15/number-of-children-waiting-six-months-for-dental-operations-soars\",\"fields\":{\"trailText\":\"Labour says 52% rise in England over three years is ‘damning indictment of Tory neglect’\",\"wordcount\":\"579\",\"thumbnail\":\"https://media.guim.co.uk/3d6627590c5824a576655bed0e98479240e6b5fe/0_1_1716_1030/500.jpg\"},\"tags\":[{\"id\":\"profile/haroonsiddique\",\"type\":\"contributor\",\"webTitle\":\"Haroon Siddique\",\"webUrl\":\"https://www.theguardian.com/profile/haroonsiddique\",\"apiUrl\":\"https://content.guardianapis.com/profile/haroonsiddique\",\"references\":[],\"bio\":\"<p>Haroon Siddique is a news reporter on the Guardian website. He previously worked for north London institution the Ham&amp;High, and before pursuing his passion for journalism he was a commodity trader in the City</p>\",\"bylineImageUrl\":\"https://uploads.guim.co.uk/2017/12/27/Haroon-Siddique.jpg\",\"bylineLargeImageUrl\":\"https://uploads.guim.co.uk/2017/12/27/Haroon_Siddique,_L.png\",\"firstName\":\"Haroon\",\"lastName\":\"Siddique\"}],\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"society/2018/jun/15/nhs-funding-budget-boost-jeremy-hunt-philip-hammond-negotiations\",\"type\":\"article\",\"sectionId\":\"society\",\"sectionName\":\"Society\",\"webPublicationDate\":\"2018-06-15T18:09:35Z\",\"webTitle\":\"Hunt and Hammond 'gridlocked' over NHS funding rise\",\"webUrl\":\"https://www.theguardian.com/society/2018/jun/15/nhs-funding-budget-boost-jeremy-hunt-philip-hammond-negotiations\",\"apiUrl\":\"https://content.guardianapis.com/society/2018/jun/15/nhs-funding-budget-boost-jeremy-hunt-philip-hammond-negotiations\",\"fields\":{\"trailText\":\"Sources say health secretary and chancellor are unable to agree size of budget boost\",\"wordcount\":\"750\",\"thumbnail\":\"https://media.guim.co.uk/451d4e2f21e007e7a39212b9584cda275342a5ac/0_199_3500_2100/500.jpg\"},\"tags\":[{\"id\":\"profile/deniscampbell\",\"type\":\"contributor\",\"webTitle\":\"Denis Campbell\",\"webUrl\":\"https://www.theguardian.com/profile/deniscampbell\",\"apiUrl\":\"https://content.guardianapis.com/profile/deniscampbell\",\"references\":[],\"bio\":\"<p>Denis Campbell is health policy editor for the Guardian and the Observer. He has written about the NHS, public health and medicine since 2007 and shares health-writing duties with Sarah Boseley, the health editor</p>\",\"bylineImageUrl\":\"https://uploads.guim.co.uk/2018/04/13/Denis-Campbell.jpg\",\"bylineLargeImageUrl\":\"https://uploads.guim.co.uk/2018/04/13/Denis_Campbell,_L.png\",\"firstName\":\"Denis\",\"lastName\":\"Campbell\"}],\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"society/2018/jun/15/social-mobility-in-richest-countries-has-stalled-since-1990s\",\"type\":\"article\",\"sectionId\":\"society\",\"sectionName\":\"Society\",\"webPublicationDate\":\"2018-06-15T16:33:33Z\",\"webTitle\":\"Social mobility in richest countries 'has stalled since 1990s'\",\"webUrl\":\"https://www.theguardian.com/society/2018/jun/15/social-mobility-in-richest-countries-has-stalled-since-1990s\",\"apiUrl\":\"https://content.guardianapis.com/society/2018/jun/15/social-mobility-in-richest-countries-has-stalled-since-1990s\",\"fields\":{\"trailText\":\"OECD report says it could take 150 years for a child from a poor UK family to earn national average\",\"wordcount\":\"675\",\"thumbnail\":\"https://media.guim.co.uk/5150c2825d01f65340e9646c904ca37a7c586f80/0_187_5616_3370/500.jpg\"},\"tags\":[{\"id\":\"profile/phillipinman\",\"type\":\"contributor\",\"webTitle\":\"Phillip Inman\",\"webUrl\":\"https://www.theguardian.com/profile/phillipinman\",\"apiUrl\":\"https://content.guardianapis.com/profile/phillipinman\",\"references\":[],\"bio\":\"<p>Phillip Inman is economics editor of the Observer and&nbsp;an economics writer for the Guardian. He is the author of Managing Your Debt, a Which? essential guide; and the Guardian e-book The Financial Crisis: How Did We Get Here?</p>\",\"bylineImageUrl\":\"https://static.guim.co.uk/sys-images/Guardian/Pix/contributor/2007/09/28/phillip_inman_140x140.jpg\",\"firstName\":\"inman\",\"lastName\":\"\"}],\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"society/2018/jun/15/why-is-theresa-may-expected-to-increase-nhs-funding-by-6bn-a-year\",\"type\":\"article\",\"sectionId\":\"society\",\"sectionName\":\"Society\",\"webPublicationDate\":\"2018-06-15T13:10:36Z\",\"webTitle\":\"Why is May expected to increase NHS funding by up to £6bn a year?\",\"webUrl\":\"https://www.theguardian.com/society/2018/jun/15/why-is-theresa-may-expected-to-increase-nhs-funding-by-6bn-a-year\",\"apiUrl\":\"https://content.guardianapis.com/society/2018/jun/15/why-is-theresa-may-expected-to-increase-nhs-funding-by-6bn-a-year\",\"fields\":{\"trailText\":\"After eight years of 1% rises, PM keen to mark 70th birthday of NHS – but deal to be finalised\",\"wordcount\":\"1004\",\"thumbnail\":\"https://media.guim.co.uk/c1f4f5316ed5e259661a41da5f5d1c91b35038e0/0_196_3879_2327/500.jpg\"},\"tags\":[{\"id\":\"profile/deniscampbell\",\"type\":\"contributor\",\"webTitle\":\"Denis Campbell\",\"webUrl\":\"https://www.theguardian.com/profile/deniscampbell\",\"apiUrl\":\"https://content.guardianapis.com/profile/deniscampbell\",\"references\":[],\"bio\":\"<p>Denis Campbell is health policy editor for the Guardian and the Observer. He has written about the NHS, public health and medicine since 2007 and shares health-writing duties with Sarah Boseley, the health editor</p>\",\"bylineImageUrl\":\"https://uploads.guim.co.uk/2018/04/13/Denis-Campbell.jpg\",\"bylineLargeImageUrl\":\"https://uploads.guim.co.uk/2018/04/13/Denis_Campbell,_L.png\",\"firstName\":\"Denis\",\"lastName\":\"Campbell\"}],\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"society/2018/jun/15/may-poised-to-boost-nhs-budget-by-up-to-6bn-a-year\",\"type\":\"article\",\"sectionId\":\"society\",\"sectionName\":\"Society\",\"webPublicationDate\":\"2018-06-14T23:57:12Z\",\"webTitle\":\"May poised to boost NHS budget by up to £6bn a year\",\"webUrl\":\"https://www.theguardian.com/society/2018/jun/15/may-poised-to-boost-nhs-budget-by-up-to-6bn-a-year\",\"apiUrl\":\"https://content.guardianapis.com/society/2018/jun/15/may-poised-to-boost-nhs-budget-by-up-to-6bn-a-year\",\"fields\":{\"trailText\":\"Prime minister will scrap eight-year policy of limiting funding to rises of only 1%\",\"wordcount\":\"763\",\"thumbnail\":\"https://media.guim.co.uk/2c06c4e1ec9f0808260a336dda161a2aedb705bb/0_46_3500_2100/500.jpg\"},\"tags\":[{\"id\":\"profile/deniscampbell\",\"type\":\"contributor\",\"webTitle\":\"Denis Campbell\",\"webUrl\":\"https://www.theguardian.com/profile/deniscampbell\",\"apiUrl\":\"https://content.guardianapis.com/profile/deniscampbell\",\"references\":[],\"bio\":\"<p>Denis Campbell is health policy editor for the Guardian and the Observer. He has written about the NHS, public health and medicine since 2007 and shares health-writing duties with Sarah Boseley, the health editor</p>\",\"bylineImageUrl\":\"https://uploads.guim.co.uk/2018/04/13/Denis-Campbell.jpg\",\"bylineLargeImageUrl\":\"https://uploads.guim.co.uk/2018/04/13/Denis_Campbell,_L.png\",\"firstName\":\"Denis\",\"lastName\":\"Campbell\"}],\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"}]}}";

    private static String LOG_TAG = NewsUtil.class.getSimpleName();

    public static List<NewsStory> fetchNewsData(String url){

        // TODO: Replace this with real HTTP request data
        return extractNewsStoriesFromJSON(data);
    }

    /**
     * Returns string as proper URL object.
     */
    private URL createUrl(String stringURL){
        URL url = null;
        try{
            url = new URL(stringURL);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG,"URL not properly formed", e);
        }
        return url;
    }

    /**
     * Makes HTTP request and returns the JSON as a String.
     */
    private static String makeHTTPRequest(URL url) throws IOException{
        String jsonResponse = "";

        if(url == null) return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream         = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else{
                Log.e(LOG_TAG,"Error response code: " + urlConnection.getResponseCode());
            }
        }catch(IOException e){
            Log.e(LOG_TAG, "Problem getting JSON.", e);
        }finally{
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
                // this may throw IOException, so much be handled
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Converts stream into string of whole JSON response.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line != null){
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }

    /**
     * Parses input JSON string into List of NewsStory objects.
     */
    private static List<NewsStory> extractNewsStoriesFromJSON(String json){
        if(TextUtils.isEmpty(json)) return null;

        ArrayList<NewsStory> newsStories = new ArrayList<>();

        try{
            JSONObject data     = new JSONObject(json);
            JSONArray results   = data.getJSONObject("response").getJSONArray("results");

            for(int i = 0; i < results.length(); i++){
                JSONObject newsObj  = results.getJSONObject(i);
                JSONObject extras   = newsObj.getJSONObject("fields");


                // REQUIRED
                String title    = newsObj.getString("webTitle");
                String section  = newsObj.getString("sectionName");

                // MUST INCLUDE IF EXISTS
                // publication data
                String date = newsObj.getString("webPublicationDate");

                //author(s)
                JSONArray tags  = newsObj.getJSONArray("tags");
                String author   = null;
                if(tags.length()>0){
                    StringJoiner joiner = new StringJoiner(",");
                    for(int j = 0; j<tags.length();j++){
                        JSONObject authorObj = tags.getJSONObject(j);
                        joiner.add(authorObj.getString("webTitle"));
                    }
                    author = joiner.toString();
                }

                // EXTRAS
                String trailText    = extras.getString("trailText");
                String thumbnail    = extras.getString("thumbnail");
                Integer wordCount   = extras.getInt("wordcount");
                String url          = newsObj.getString("webUrl");
                newsStories.add(new NewsStory(wordCount,title, section, date, author, trailText, thumbnail,url));
            }
        }catch(JSONException e){
            Log.e(LOG_TAG,"Problem parsons JSON", e);
        }

        return newsStories;
    }
}
