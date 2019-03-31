package com.example.Hygienic;

import android.content.Context;
import android.widget.ImageView;

import com.example.recyclerapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Class helps select the hygiene rating image. Images contained in drawable folder.
 * It also allows correct image binding to RecyclerView by recording the URLs of images.
 *
 * @author  Kashif Ahmed - 18061036
 * @version 1.0
 * @since   March 2019
 */
public class GetCorrectRatingImage {
    private Context context;

    // Constructor
    public GetCorrectRatingImage(Context context){
        this.context = context;
    }

    /**
     * Method takes restaurant object, gets its hygiene rating and returns ImageView
     * containing appropriate official Food Standards Agency rating images.
     * @param rating Restaurant rating.
     * @return ImageView containing appropriate rating image.
     */
    public ImageView getRatingImage(String rating){
        ImageView ratingImage = new ImageView(context);
        switch (rating){
            case "G":  // G is last character of 'EXEMPT FROM RATING'
                ratingImage.setBackgroundResource(R.drawable.exempt);
                break;
            case "0":
                ratingImage.setBackgroundResource(R.drawable.zero);
                break;
            case "1":
                ratingImage.setBackgroundResource(R.drawable.one);
                break;
            case "2":
                ratingImage.setBackgroundResource(R.drawable.two);
                break;
            case "3":
                ratingImage.setBackgroundResource(R.drawable.three);
                break;
            case "4":
                ratingImage.setBackgroundResource(R.drawable.four);
                break;
            case "5":
                ratingImage.setBackgroundResource(R.drawable.five);
                break;
            default:
                ratingImage.setBackgroundResource(R.drawable.exempt);
                break;
        }

        return ratingImage;
    }

    /**
     * This method allows binding of correct Hygiene rating images in RecyclerView.
     *
     * @param restaurants List of restaurants returned from API.
     * @return List<String> containing URL links to rating images.
     */
    public static List<String> generateRatingImagesForRecyclerView(List<Restaurant> restaurants){
        List<String> imageURLs = new ArrayList<>();
        for (Restaurant r : restaurants){
            char ratingValue = r.getRatingValue().charAt(r.getRatingValue().length() - 1);
            switch (ratingValue){
                case 'G':  // G is last character of 'EXEMPT FROM RATING'
                    imageURLs.add("https://i.ibb.co/72sxYVL/exempt.png");
                    break;
                case '0':
                    imageURLs.add("https://i.ibb.co/6Xg4PDz/zero.png");
                    break;
                case '1':
                    imageURLs.add("https://i.ibb.co/YkPY2M7/one.png");
                    break;
                case '2':
                    imageURLs.add("https://i.ibb.co/dksZxKS/two.png");
                    break;
                case '3':
                    imageURLs.add("https://i.ibb.co/dDXSFVq/three.png");
                    break;
                case '4':
                    imageURLs.add("https://i.ibb.co/hcNKcVC/four.png");
                    break;
                case '5':
                    imageURLs.add("https://i.ibb.co/VM12X4Y/five.png");
                    break;
                default:
                    imageURLs.add("https://i.ibb.co/72sxYVL/exempt.png");
                    break;
            }
        }
        return imageURLs;
    }
}