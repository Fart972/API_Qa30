
package demoqa;

        import okhttp3.OkHttpClient;
        import okhttp3.Request;
        import okhttp3.Response;
        import org.openqa.selenium.By;
        import org.openqa.selenium.JavascriptExecutor;
        import org.openqa.selenium.WebDriver;
        import org.openqa.selenium.WebElement;
        import org.openqa.selenium.chrome.ChromeDriver;
        import org.testng.annotations.AfterMethod;
        import org.testng.annotations.BeforeMethod;
        import org.testng.annotations.Test;

        import java.util.List;

        import static com.jayway.restassured.RestAssured.given;

public class BrokenLinkTest {

    WebDriver wd;

    @BeforeMethod
    public void init() {
        wd = new ChromeDriver();
        wd.navigate().to("https://demoqa.com/broken");
        wd.manage().window().maximize();
        hideFooter();

        if (isElementPresent(By.id("close-fixedban"))) {
            wd.findElement(By.id("close-fixedban")).click();
        }

    }

    @Test
    public void brokenLinksTest() {
        checkBrokenLinks();
    }

    @Test
    public void brokenImageTest() {
        checkBrokenImages();
    }

    @AfterMethod
    public void stop() {
        wd.quit();
    }


//*********************************************************


    public void checkBrokenLinks() {
        List<WebElement> links = wd.findElements(By.cssSelector("a[href]"));
        System.out.println("Total links on the Wb Page: " + links.size());
        for (WebElement el : links) {
            String url = el.getAttribute("href");

            verifyLinksRestAssured(url);
            // verifyLinksOkHttp(url);
        }

    }


    private void verifyLinksOkHttp(String linkUrl) {

        OkHttpClient client = new OkHttpClient();
        try {
            Request request = new Request.Builder()
                    .url(linkUrl)
                    .head()
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                System.out.println(linkUrl + " - " + response.code() + " " + response.message());
            } else {
                System.out.println(linkUrl + " - " + response.code() + " " + response.message() + " is a broken link");
            }
        } catch (Exception e) {
            System.out.println("You have error " + e.getMessage());
        }
    }

    private void verifyLinksRestAssured(String linkUrl) {

        try {
            int code = given().when().head(linkUrl).then().extract().statusCode();
            if(code==200){
                System.out.println(linkUrl + " - " + code + "  "+" NOT BROKEN");
            } else{
                System.out.println(linkUrl + " - " + code + " " + " is a broken link");
            }
        } catch(Exception e) {
            System.out.println(linkUrl);
            System.out.println("You have error " + e.getMessage());
        }

    }


    public void checkBrokenImages() {
        List<WebElement> images = wd.findElements(By.tagName("img"));

        System.out.println("We have " + images.size() + " " + "images");
        //checking the links fetched.

//        for (int index = 0; index < images.size(); index++) {
//
//            WebElement image = images.get(index);
//            String imageURL = image.getAttribute("src");
//            System.out.println("URL of Image " + (index + 1) + " is: " + imageURL);
//            verifyLinksOkHttp(imageURL);

        for (WebElement image:images) {
            String imageURL = image.getAttribute("src");
            System.out.println("URL of Image " + "  " + " is: " + imageURL);
            // verifyLinksOkHttp(imageURL);
            verifyLinksRestAssured(imageURL);



            //Validate image display using JavaScript executor
            //document.querySelector('img').naturalWidth
            //typeof document.querySelector('img').naturalWidth
            //document.querySelectorAll('img').forEach(i => console.log(typeof i.naturalWidth));
            try {
                //JavascriptExecutor js = (JavascriptExecutor) wd;

                boolean imageDisplayed = (Boolean) ((JavascriptExecutor) wd)
                        .executeScript("return (typeof arguments[0].naturalWidth != undefined && arguments[0].naturalWidth > 0);", image);
                if (imageDisplayed) {
                    System.out.println("DISPLAY - OK");
                    System.out.println("*******************");
                } else {
                    System.out.println("DISPLAY - BROKEN");
                    System.out.println("*******************");
                }
            } catch (Exception e) {
                System.out.println("Error Occured");
            }
        }
    }

    public void hideFooter() {

        JavascriptExecutor js = (JavascriptExecutor) wd;
        js.executeScript("document.querySelector('footer').style.display='none';");
    }

    public boolean isElementPresent(By locator) {
        return wd.findElements(locator).size() > 0;
    }
}