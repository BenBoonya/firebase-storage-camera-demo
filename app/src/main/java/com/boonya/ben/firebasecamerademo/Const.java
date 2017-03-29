package com.boonya.ben.firebasecamerademo;

/**
 * Created by Boonya Kitpitak on 3/26/17.
 */

public class Const {
    public static class Intent {
        public static final int REQUEST_CROP = 123;
        public static final int REQUEST_CAMERA = 234;
        public static final int REQUEST_GALLERY = 345;
    }

    public static class Firebase {
        public static final String FIREBASE_FOOD_DIRECTORY = "Food";
        public static final String FIREBASE_IMAGE_INFO_REF = "ImagesInformation";
    }

    public static class Extra {
        public static final String IMAGE_URI_EXTRA = "image_uri";
    }
}
