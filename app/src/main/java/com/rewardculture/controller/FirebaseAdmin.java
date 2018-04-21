package com.rewardculture.controller;

/**
 * Upload data to Firebase.
 * <p>
 * TODO add test database (for testing only).
 * TODO upload dictionary
 */

public class FirebaseAdmin {

//    private static final String PUSHED_DATA_FILE = "core/data/pushed_data";
//
//    static JSONArray readJson(String file) throws IOException {
//        BufferedReader reader = new BufferedReader(new FileReader(file));
//        JSONTokener tokener = new JSONTokener(reader);
//        JSONArray jsonArray = new JSONArray(tokener);
//        reader.close();
//        return jsonArray;
//    }
//
//    static String coreDataPath(String file) {
//        return String.format("core/data/bbc-%s.json", file);
//    }
//
//    static String getCollectionEpisodesJsonFile(String collectionTitle) {
//        Map<String, String> map = new HashMap<>();
//        map.put("News Report", coreDataPath("news-report"));
//        map.put("Lingohack", coreDataPath("lingohack"));
//        map.put("6 Minute English", coreDataPath("sme"));
//        map.put("A Christmas Carol", coreDataPath("drama-carol"));
//        map.put("Alice in Wonderland", coreDataPath("drama-aliceinwonderland"));
//        map.put("Jamaica Inn", coreDataPath("drama-jamaica"));
//        map.put("Guilliver's Travels", coreDataPath("drama-gulliver"));
//        map.put("The Importance of Being Earnest", coreDataPath("drama-earnest"));
//        map.put("Frankenstein", coreDataPath("drama-frankenstein"));
//        return map.get(collectionTitle);
//    }
//
//    static String concatTitles(String collectionTitle, String lessonTitle) {
//        return String.format("%s/%s", collectionTitle, lessonTitle);
//    }
//
//    /**
//     * Push whole data to Google Firebase.
//     *
//     * @param db
//     * @throws IOException
//     */
//    static void push(FirebaseDatabase db) throws IOException, ClassNotFoundException {
//        // Read from storage collections and lessons that are already on Firebase
//        Map<String, String> pushedCollections;
//        Map<String, String> pushedLessons;
//        Object[] objs = readPushedData(PUSHED_DATA_FILE);
//        pushedCollections = (Map<String, String>) objs[0];
//        pushedLessons = (Map<String, String>) objs[1];
//
//        String file = "core/data/collections.json";
//        JSONArray collections = readJson(file);
//
//        DatabaseReference collectionsRef = db.getReference("collections");
//        DatabaseReference lessonsRef = db.getReference("lessons");
//        DatabaseReference collectionRef, lessonRef;
//        try {
//            for (int i = 0; i < collections.length(); i++) {
//                Collection c = Collection.fromJsonObject((JSONObject) collections.get(i));
//                String episodesJsonFile = getCollectionEpisodesJsonFile(c.title);
//                // Only push collections that are checked
//                if (episodesJsonFile != null) {
//                    System.out.println(String.format("...pushing %s", c.title));
//                    if (!pushedCollections.containsKey(c.title)) {
//                        collectionRef = collectionsRef.push();
//                    } else {
//                        collectionRef = db.getReference("collections").child(pushedCollections.get(c.title));
//                    }
//
//                    // Push collection's episodes
//                    JSONArray episodes = readJson(episodesJsonFile);
//                    Map<String, String> lessonIdToTitleMap = new HashMap<>();
//                    for (int j = 0; j < episodes.length(); j++) {
//                        Lesson lesson = Lesson.fromJsonObject((JSONObject) episodes.get(j));
//                        String concatedTitle = concatTitles(c.title, lesson.title);
//                        if (!pushedLessons.containsKey(concatedTitle)) {
//                            lessonRef = lessonsRef.push();
//                        } else {
//                            lessonRef = db.getReference("lessons").child(pushedLessons.get(concatedTitle));
//                        }
//                        System.out.println(String.format("...writing or replacing episode %s at %s",
//                                lesson.title, lessonRef.getKey()));
//                        lessonRef.setValue(lesson);
//                        lessonIdToTitleMap.put(lessonRef.getKey(), lesson.title);
//                        pushedLessons.put(concatedTitle, lessonRef.getKey());
//                    }
//
//                    // Write or replace collection
//                    System.out.println(String.format("...writing or replacing collection at %s",
//                            collectionRef.getKey()));
//                    c.lessonIdToTitleMap = lessonIdToTitleMap;
//                    pushedCollections.put(c.title, collectionRef.getKey());
//                    collectionRef.setValue(c);
//                }
//            }
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//            e.printStackTrace();
//        } finally {
//            writePushedData(pushedCollections, pushedLessons);
//        }
//    }
//
//    public static Object[] readPushedData(String file) throws IOException, ClassNotFoundException {
//        Object[] results = new Object[2];
//        if (Files.exists(FileSystems.getDefault().getPath("", PUSHED_DATA_FILE))) {
//            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PUSHED_DATA_FILE));
//            results[0] = ois.readObject();
//            results[1] = ois.readObject();
//            ois.close();
//            assert results[0] instanceof Map;
//            assert results[1] instanceof Map;
//        } else {
//            results[0] = new HashMap<String, String>();
//            results[1] = new HashMap<String, String>();
//        }
//
//        return results;
//    }
//
//    /**
//     * Save pushed data so that we won't push again.
//     *
//     * @param pushedCollections
//     * @param pushedLessons
//     * @throws IOException
//     */
//    public static void writePushedData(Map<String, String> pushedCollections, Map<String, String> pushedLessons)
//            throws IOException {
//        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
//                PUSHED_DATA_FILE));
//        oos.writeObject(pushedCollections);
//        oos.writeObject(pushedLessons);
//        oos.close();
//    }
//
//    public static void checkPushedData() throws IOException, ClassNotFoundException {
//        Object[] results = FirebaseAdmin.readPushedData("core/data/pushed_data");
//        Map<String, String> collections = (Map<String, String>) results[0];
//        System.out.println(collections);
//        System.out.println();
//        Map<String, String> lessons = (Map<String, String>) results[1];
//        System.out.println(lessons);
//    }
//
//
//    public static void main(String args[]) throws IOException, ClassNotFoundException {
//        FirebaseOptions options = new FirebaseOptions.Builder()
//                .setServiceAccount(
//                        new FileInputStream("core/firebase-service-account"))
//                .setDatabaseUrl("https://english-listening-84d9c.firebaseio.com")
//                .build();
//
//        FirebaseApp.initializeApp(options);
//        // FirebaseAuth defaultAuth = FirebaseAuth.getInstance();
//        FirebaseDatabase defaultDatabase = FirebaseDatabase.getInstance();
//        push(defaultDatabase);
//    }
}
