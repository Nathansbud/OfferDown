import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import org.jsoup.Connection;

import java.io.IOException;
import java.util.List;

public class OfferDown {
    enum Category {
        ANTIQUES("antiques"),
        APPLIANCES("appliances"),
        ARTSANDCRAFTS("arts-crafts"),
        AUDIO("audio-equipment"),
        CAR("auto-parts"),
        BABY("baby-kids"),
        BEAUTY("beauty-health"),
        BICYCLES("bicycles"),
        BOATS("boats-marine"),
        BOOKS("books-magazines"),
        BUSINESS("business-equipment"),
        PHONES("cell-phones"),
        CDS("cds-dvds"),
        COLLECTIBLES("collectibles"),
        COMPUTER("computer-equipment"),
        ELECTRONICS("electronics"),
        EXERCISE("exercise"),
        FREE("free"),
        FURNITURE("furniture"),
        GAMES("games-toys"),
        VIDEOGAMES("video-games"),
        BOARDGAMES("board-games"),
        GENERAL("general"),
        HOME("home-garden"),
        JEWELRY("jewelry-accessories"),
        MUSIC("musical-instruments"),
        SPORTS("sports-outdoors"),

        NONE();

        Category() {
            name = null;
        }
        Category(String _name) {
            name = _name;
        }
        final String name;

        public String getName() {
            return name;
        }
        public boolean hasName() {
            return name != null;
        }
    }

    private static Category currentCategory = Category.BOARDGAMES;

    public static void downloadPage() {
        try {
            Document d = Jsoup.connect("https://offerup.com/" +
                            ((currentCategory != Category.NONE) ?
                                ("explore/k/" + currentCategory.getName()) :
                                (""))
                    ).get();

//            Element allItems = d.getElementById("db-item-list");

            //_109rpto _1anrh0x is link (item top node) class name
            //_19rx43s2 is loc
            //_s3g03e4 is price
            //_nn5xny4 _y9ev9r is item name
            Elements itemList = d.getElementsByClass("_109rpto _1anrh0x");

            for(Element item : itemList) {
                String itemLink = "https://offerup.com";
                if(item.attr("href").startsWith("/item/")) {
                    itemLink += item.attr("href");
                } else {
                    continue;
                }

                String itemName = item.child(0).child(1).child(0).text();
                String itemCost = item.child(0).child(1).child(1).text();
                String itemLocation = item.child(0).child(1).child(2).text();
                if(!itemCost.equals("SOLD")) {
                    if(itemCost.equals("FREE")) {
                        System.out.println(itemName + " (" + itemCost + ", " + itemLocation + ") [Link: " + itemLink + "]");
                    } else {
                        if(Float.parseFloat(itemCost.replaceAll("[^\\d.]", "")) < 20) {
                            System.out.println(itemName + " (" + itemCost + ", " + itemLocation + ") [Link: " + itemLink + "]");
                        }
                    }
                }
            }

            Connection.Response req;
//            String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36";
            req = Jsoup.connect("https://offerup-rest.ably.io/keys/ihXixg.D0_jDg/requestToken?rnd=5831768895139446")
//                    .userAgent(userAgent)
                    .data("username", "daddy", "password", "hunter2")
                    .method(Connection.Method.POST)
                    .followRedirects(true).execute();


            Document doe = Jsoup.connect("https://offerup.com").cookies(req.cookies()).get();
            System.out.println(doe.body().text());
        } catch(IOException e) {
            System.out.println(":((((");
        }
    }


    public static void main(String[] args) {

        downloadPage();
    }
}
