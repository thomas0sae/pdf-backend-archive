package com.libgen;

import com.google.gson.Gson;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Set;

public class LibGenImageDownload {
    private static MessageDigest md;

    static {

        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nse) {
            nse.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        LibGenImageDownload libGenImageDownload = new LibGenImageDownload();
        HashMap<String,String> topicMap = libGenImageDownload.getTopicMap();
        for (long l = 558; l < 900; l++) {
            Writer libGenImageDownloadJsonWriter = getElasticJsonFileWriter();
            libGenImageDownload.callAndWriteNonFictionData(libGenImageDownloadJsonWriter, l, topicMap);
        }
    }

    private static Writer getElasticJsonFileWriter() throws IOException {
        File postProcessedPDFRecordForUIJson = new File("elastic-libgen-upload" + System.currentTimeMillis() + ".json");
        Writer postProcessedPDFRecordForUIJsonWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(postProcessedPDFRecordForUIJson), StandardCharsets.UTF_8));
        return postProcessedPDFRecordForUIJsonWriter;
    }

    private static String generateImageFileName(String fileName) {
        StringBuilder pathBaseName = new StringBuilder("pdf-images").append("/").append(fileName, 0, 3).append("/")
                .append(fileName).append(".jpg");
        File imageFilePath = new File(pathBaseName.toString());
        imageFilePath.getParentFile().mkdirs();
        return pathBaseName.toString();
    }

    public static void saveImage(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destinationFile);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }
        is.close();
        os.close();
    }

    public void callAndWriteNonFictionData(Writer libGenImageDownloadJsonWriter, long l, HashMap<String,String> topicMap) throws Exception {
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:libgen.db");
        int size = 1000;
        Statement stat = conn.createStatement();
        stat.setFetchSize(size/10);
        String sql = "select Id, Title, VolumeInfo, Series, Periodical, Authors, Year, Edition, Publisher, City, Pages, PagesInFile, Language, Topic, Library, Issue, Identifier, Issn, Asin, Udc, Lbc, Ddc, Lcc, Doi, GoogleBookId, OpenLibraryId, Commentary, Dpi, Color, Cleaned, Orientation, Paginated, Scanned, Bookmarked, Searchable, SizeInBytes, Format, Md5Hash, Generic, Visible, Locator, Local, AddedDateTime, LastModifiedDateTime, CoverUrl, Tags, IdentifierPlain, LibgenId, FileId from non_fiction where Format='pdf' order by id asc limit " + (l * size) + " , " + size;
        System.out.println(sql);
        ResultSet rs = stat.executeQuery(sql);
        String[] urlArray = new String[]{
                "http://libgen.rs/covers/",
                "http://library.lol/covers/",
                "https://libgen.lc/covers/",
                "http://gen.lib.rus.ec/covers/",
                "http://i.booklid.org/covers/"
        };
        while (rs.next()) {
            Thread.sleep(900);
            LibGenNonFiction libGenNonFiction = new LibGenNonFiction();
            {

                libGenNonFiction.setId(rs.getLong("Id"));
                libGenNonFiction.setTitle(rs.getString("Title"));
                libGenNonFiction.setVolumeInfo(rs.getString("VolumeInfo"));
                libGenNonFiction.setSeries(rs.getString("Series"));
                libGenNonFiction.setPeriodical(rs.getString("Periodical"));
                libGenNonFiction.setAuthors(rs.getString("Authors"));
                libGenNonFiction.setYear(rs.getString("Year"));
                libGenNonFiction.setEdition(rs.getString("Edition"));
                libGenNonFiction.setPublisher(rs.getString("Publisher"));
                libGenNonFiction.setCity(rs.getString("City"));
                libGenNonFiction.setPages(rs.getString("Pages"));
                libGenNonFiction.setPagesInFile(rs.getLong("PagesInFile"));
                libGenNonFiction.setLanguage(rs.getString("Language"));
                libGenNonFiction.setTopic(topicMap.get(rs.getString("Topic")));
                libGenNonFiction.setLibrary(rs.getString("Library"));
                libGenNonFiction.setIssue(rs.getString("Issue"));
                libGenNonFiction.setIdentifier(rs.getString("Identifier"));
                libGenNonFiction.setIssn(rs.getString("Issn"));
                libGenNonFiction.setAsin(rs.getString("Asin"));
                libGenNonFiction.setUdc(rs.getString("Udc"));
                libGenNonFiction.setLbc(rs.getString("Lbc"));
                libGenNonFiction.setDdc(rs.getString("Ddc"));
                libGenNonFiction.setLcc(rs.getString("Lcc"));
                libGenNonFiction.setDoi(rs.getString("Doi"));
                libGenNonFiction.setGoogleBookId(rs.getString("GoogleBookId"));
                libGenNonFiction.setOpenLibraryId(rs.getString("OpenLibraryId"));
                libGenNonFiction.setCommentary(rs.getString("Commentary"));
                libGenNonFiction.setDpi(rs.getLong("Dpi"));
                libGenNonFiction.setColor(rs.getString("Color"));
                libGenNonFiction.setCleaned(rs.getString("Cleaned"));
                libGenNonFiction.setOrientation(rs.getString("Orientation"));
                libGenNonFiction.setPaginated(rs.getString("Paginated"));
                libGenNonFiction.setScanned(rs.getString("Scanned"));
                libGenNonFiction.setBookmarked(rs.getString("Bookmarked"));
                libGenNonFiction.setSearchable(rs.getString("Searchable"));
                libGenNonFiction.setSizeInBytes(rs.getLong("SizeInBytes"));
                libGenNonFiction.setFormat(rs.getString("Format"));
                libGenNonFiction.setMd5Hash(rs.getString("Md5Hash"));
                libGenNonFiction.setGeneric(rs.getString("Generic"));
                libGenNonFiction.setVisible(rs.getString("Visible"));
                libGenNonFiction.setLocator(rs.getString("Locator"));
                libGenNonFiction.setLocal(rs.getLong("Local"));
                libGenNonFiction.setAddedDateTime(rs.getString("AddedDateTime"));
                libGenNonFiction.setLastModifiedDateTime(rs.getString("LastModifiedDateTime"));
                libGenNonFiction.setCoverUrl(rs.getString("CoverUrl"));
                libGenNonFiction.setTags(rs.getString("Tags"));
                libGenNonFiction.setIdentifierPlain(rs.getString("IdentifierPlain"));
                libGenNonFiction.setLibgenId(rs.getLong("LibgenId"));
                libGenNonFiction.setFileId(rs.getLong("FileId"));
                String md5 = rs.getString("Md5Hash").toLowerCase();
                String imgPath = generateImageFileName(md5);
                libGenNonFiction.setImgFlPth(imgPath);
                try {
                    for (String url : urlArray) {
                        saveImage(url + rs.getString("CoverUrl"), imgPath);
                        break;
                    }
                    System.out.println(libGenNonFiction);

                    libGenImageDownloadJsonWriter.append("{\"index\" : {\"_id\":\"" + md5 + "\"}}").append("\r\n");
                    libGenImageDownloadJsonWriter.append((toJson(libGenNonFiction))).append("\r\n");
                    libGenImageDownloadJsonWriter.flush();
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                    //System.out.println("Failed "+url + rs.getString("CoverUrl"));
                }
            }
        }
        rs.close();
        conn.close();
    }

    /**
     * convert object to json
     */
    public String toJson(Object obj) {
        // Convert emtpy string and objects to null so we don't serialze them
        setEmtpyStringsAndObjectsToNull(obj);
        return new Gson().toJson(obj);
    }

    /**
     * Sets all empty strings and objects (all fields null) including sets to null.
     *
     * @param obj any object
     */
    public void setEmtpyStringsAndObjectsToNull(Object obj) {
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object fieldObj = field.get(obj);
                if (fieldObj != null) {
                    Class fieldType = field.getType();
                    if (fieldType.isAssignableFrom(String.class)) {
                        if (fieldObj.equals("")) {
                            field.set(obj, null);
                        }
                    } else if (fieldType.isAssignableFrom(Set.class)) {
                        for (Object item : (Set) fieldObj) {
                            setEmtpyStringsAndObjectsToNull(item);
                        }
                        boolean setFielToNull = true;
                        for (Object item : (Set) field.get(obj)) {
                            if (item != null) {
                                setFielToNull = false;
                                break;
                            }
                        }
                        if (setFielToNull) {
                            setFieldToNull(obj, field); 
                        }
                    } else if (!isPrimitiveOrWrapper(fieldType)) {
                        setEmtpyStringsAndObjectsToNull(fieldObj);
                        boolean setFielToNull = true;
                        for (Field f : fieldObj.getClass().getDeclaredFields()) {
                            f.setAccessible(true);
                            if (f.get(fieldObj) != null) {
                                setFielToNull = false;
                                break;
                            }
                        }
                        if (setFielToNull) {
                            setFieldToNull(obj, field);
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                System.err.println("Error while setting empty string or object to null: " + e.getMessage());
            }
        }
    }

    private void setFieldToNull(Object obj, Field field) throws IllegalAccessException {
        if (!Modifier.isFinal(field.getModifiers())) {
            field.set(obj, null);
        }
    }

    private boolean isPrimitiveOrWrapper(Class fieldType) {
        return fieldType.isPrimitive()
                || fieldType.isAssignableFrom(Integer.class)
                || fieldType.isAssignableFrom(Boolean.class)
                || fieldType.isAssignableFrom(Byte.class)
                || fieldType.isAssignableFrom(Character.class)
                || fieldType.isAssignableFrom(Float.class)
                || fieldType.isAssignableFrom(Long.class)
                || fieldType.isAssignableFrom(Double.class)
                || fieldType.isAssignableFrom(Short.class);
    }

    public HashMap getTopicMap()
    {
        HashMap topiMap = new HashMap<String,String>();
        topiMap.put("1","Business");
        topiMap.put("2","Business\\Accounting");
        topiMap.put("3","Business\\Logistics");
        topiMap.put("4","Business\\Marketing");
        topiMap.put("5","Business\\Marketing: Advertising");
        topiMap.put("6","Business\\Management");
        topiMap.put("7","Business\\Management: Project Management");
        topiMap.put("8","Business\\MLM");
        topiMap.put("9","Business\\Responsibility and Business Ethics");
        topiMap.put("10","Business\\Trading");
        topiMap.put("11","Business\\E-Commerce");
        topiMap.put("12","Biology");
        topiMap.put("13","Biology\\Natural Science");
        topiMap.put("14","Biology\\Anthropology");
        topiMap.put("15","Biology\\Anthropology: Evolution");
        topiMap.put("16","Biology\\Biostatistics");
        topiMap.put("17","Biology\\Biotechnology");
        topiMap.put("18","Biology\\Biophysics");
        topiMap.put("19","Biology\\Biochemistry");
        topiMap.put("20","Biology\\Biochemistry: enologist");
        topiMap.put("21","Biology\\Virology");
        topiMap.put("22","Biology\\Genetics");
        topiMap.put("23","Biology\\Zoology");
        topiMap.put("24","Biology\\Zoology: Paleontology");
        topiMap.put("25","Biology\\Zoology: Fish");
        topiMap.put("26","Biology\\Microbiology");
        topiMap.put("27","Biology\\Molecular");
        topiMap.put("28","Biology\\Molecular: Bioinformatics");
        topiMap.put("29","Biology\\Plants: Botany");
        topiMap.put("30","Biology\\Plants: Agriculture and Forestry");
        topiMap.put("31","Biology\\Ecology");
        topiMap.put("32","Geography");
        topiMap.put("33","Geography\\Geodesy. Cartography");
        topiMap.put("34","Geography\\Local History");
        topiMap.put("35","Geography\\Local history: Tourism");
        topiMap.put("36","Geography\\Meteorology, Climatology");
        topiMap.put("37","Geography\\Russia");
        topiMap.put("38","Geology");
        topiMap.put("39","Geology\\Hydrogeology");
        topiMap.put("40","Geology\\Mining");
        topiMap.put("41","Housekeeping, leisure");
        topiMap.put("42","Housekeeping, leisure\\Aquaria");
        topiMap.put("43","Housekeeping, leisure\\Astrology");
        topiMap.put("44","Housekeeping, leisure\\Pet");
        topiMap.put("45","Housekeeping, leisure\\Games: Board Games");
        topiMap.put("46","Housekeeping, leisure\\Games: Chess");
        topiMap.put("47","Housekeeping, leisure\\Collecting");
        topiMap.put("48","Housekeeping, leisure\\Beauty, image");
        topiMap.put("49","Housekeeping, leisure\\Cooking");
        topiMap.put("50","Housekeeping, leisure\\Fashion, Jewelry");
        topiMap.put("51","Housekeeping, leisure\\Hunting and Game Management");
        topiMap.put("52","Housekeeping, leisure\\Benefits Homebrew");
        topiMap.put("53","Housekeeping, leisure\\Professions and Trades");
        topiMap.put("54","Housekeeping, leisure\\Handicraft");
        topiMap.put("55","Housekeeping, leisure\\Handicraft: Cutting and Sewing");
        topiMap.put("56","Housekeeping, leisure\\Garden, garden");
        topiMap.put("57","Art");
        topiMap.put("58","Art\\Design: Architecture");
        topiMap.put("59","Art\\Graphic Arts");
        topiMap.put("60","Art\\Cinema");
        topiMap.put("61","Art\\Music");
        topiMap.put("62","Art\\Music: Guitar");
        topiMap.put("63","Art\\Photo");
        topiMap.put("64","History");
        topiMap.put("65","History\\American Studies");
        topiMap.put("66","History\\Archaeology");
        topiMap.put("67","History\\Military History");
        topiMap.put("68","History\\Memoirs, Biographies");
        topiMap.put("69","Computers");
        topiMap.put("70","Computers\\Web-design");
        topiMap.put("71","Computers\\Algorithms and Data Structures");
        topiMap.put("72","Computers\\Algorithms and Data Structures: Cryptography");
        topiMap.put("73","Computers\\Algorithms and Data Structures: Image Processing");
        topiMap.put("74","Computers\\Algorithms and Data Structures: Pattern Recognition");
        topiMap.put("75","Computers\\Algorithms and Data Structures: Digital watermarks");
        topiMap.put("76","Computers\\Databases");
        topiMap.put("77","Computers\\Security");
        topiMap.put("78","Computers\\Information Systems");
        topiMap.put("79","Computers\\Information Systems: EC businesses");
        topiMap.put("80","Computers\\Cybernetics");
        topiMap.put("81","Computers\\Cybernetics: Artificial Intelligence");
        topiMap.put("82","Computers\\Cryptography");
        topiMap.put("83","Computers\\Lectures, monographs");
        topiMap.put("84","Computers\\Media");
        topiMap.put("85","Computers\\Operating Systems");
        topiMap.put("86","Computers\\Organization and Data Processing");
        topiMap.put("87","Computers\\Programming");
        topiMap.put("88","Computers\\Programming: Libraries API");
        topiMap.put("89","Computers\\Programming: Games");
        topiMap.put("90","Computers\\Programming: Compilers");
        topiMap.put("91","Computers\\Programming: Modeling languages");
        topiMap.put("92","Computers\\Programming: Programming Languages");
        topiMap.put("93","Computers\\Programs: TeX, LaTeX");
        topiMap.put("94","Computers\\Software: Office software");
        topiMap.put("95","Computers\\Software: Adobe Products");
        topiMap.put("96","Computers\\Software: Macromedia Products");
        topiMap.put("97","Computers\\Software: CAD");
        topiMap.put("98","Computers\\Software: Systems: scientific computing");
        topiMap.put("99","Computers\\Networking");
        topiMap.put("100","Computers\\Networking: Internet");
        topiMap.put("101","Computers\\System Administration");
        topiMap.put("102","Literature");
        topiMap.put("103","Literature\\Fiction");
        topiMap.put("104","Literature\\Library");
        topiMap.put("105","Literature\\Detective");
        topiMap.put("106","Literature\\Children");
        topiMap.put("107","Literature\\Comics");
        topiMap.put("108","Literature\\Literary");
        topiMap.put("109","Literature\\Poetry");
        topiMap.put("110","Literature\\Prose");
        topiMap.put("111","Literature\\Folklore");
        topiMap.put("112","Literature\\Fantasy");
        topiMap.put("113","Mathematics");
        topiMap.put("114","Mathematics\\Algebra");
        topiMap.put("115","Mathematics\\Algebra: Linear Algebra");
        topiMap.put("116","Mathematics\\Algorithms and Data Structures");
        topiMap.put("117","Mathematics\\Analysis");
        topiMap.put("118","Mathematics\\Wavelets and signal processing");
        topiMap.put("119","Mathematics\\Probability");
        topiMap.put("120","Mathematics\\Computational Mathematics");
        topiMap.put("121","Mathematics\\Geometry and Topology");
        topiMap.put("122","Mathematics\\Puzzle");
        topiMap.put("123","Mathematics\\Dynamical Systems");
        topiMap.put("124","Mathematics\\Discrete Mathematics");
        topiMap.put("125","Mathematics\\Differential Equations");
        topiMap.put("126","Mathematics\\Combinatorics");
        topiMap.put("127","Mathematics\\The complex variable");
        topiMap.put("128","Mathematics\\Computer Algebra");
        topiMap.put("129","Mathematics\\Lectures");
        topiMap.put("130","Mathematics\\Logic");
        topiMap.put("131","Mathematics\\Mathematicsematical Statistics");
        topiMap.put("132","Mathematics\\Mathematicsematical Physics");
        topiMap.put("133","Mathematics\\Continued fractions");
        topiMap.put("134","Mathematics\\Fuzzy Logic and Applications");
        topiMap.put("135","Mathematics\\Optimal control");
        topiMap.put("136","Mathematics\\Optimization. Operations Research");
        topiMap.put("137","Mathematics\\Applied Mathematicsematics");
        topiMap.put("138","Mathematics\\Symmetry and group");
        topiMap.put("139","Mathematics\\Automatic Control Theory");
        topiMap.put("140","Mathematics\\Graph Theory");
        topiMap.put("141","Mathematics\\Game Theory");
        topiMap.put("142","Mathematics\\Operator Theory");
        topiMap.put("143","Mathematics\\Number Theory");
        topiMap.put("144","Mathematics\\Functional Analysis");
        topiMap.put("145","Mathematics\\Numerical Analysis");
        topiMap.put("146","Mathematics\\Elementary");
        topiMap.put("147","Medicine");
        topiMap.put("148","Medicine\\Anatomy and physiology");
        topiMap.put("149","Medicine\\Anesthesiology and Intensive Care");
        topiMap.put("150","Medicine\\Diseases");
        topiMap.put("151","Medicine\\Diseases: Internal Medicine");
        topiMap.put("152","Medicine\\Histology");
        topiMap.put("153","Medicine\\Homeopathy");
        topiMap.put("154","Medicine\\Dermatology");
        topiMap.put("155","Medicine\\Diabetes");
        topiMap.put("156","Medicine\\immunology");
        topiMap.put("157","Medicine\\Infectious diseases");
        topiMap.put("158","Medicine\\Yoga");
        topiMap.put("159","Medicine\\Cardiology");
        topiMap.put("160","Medicine\\Chinese Medicine");
        topiMap.put("161","Medicine\\Clinical Medicine");
        topiMap.put("162","Medicine\\Molecular Medicine");
        topiMap.put("163","Medicine\\Natural Medicine");
        topiMap.put("164","Medicine\\Popular scientific literature");
        topiMap.put("165","Medicine\\Neurology");
        topiMap.put("166","Medicine\\Oncology");
        topiMap.put("167","Medicine\\ENT");
        topiMap.put("168","Medicine\\Ophthalmology");
        topiMap.put("169","Medicine\\Pediatrics");
        topiMap.put("170","Medicine\\Dentistry, Orthodontics");
        topiMap.put("171","Medicine\\Trial");
        topiMap.put("172","Medicine\\Therapy");
        topiMap.put("173","Medicine\\Pharmacology");
        topiMap.put("174","Medicine\\Feng Shui");
        topiMap.put("175","Medicine\\Surgery, Orthopedics");
        topiMap.put("176","Medicine\\Endocrinology");
        topiMap.put("177","Medicine\\Epidemiology");
        topiMap.put("178","Science (General)");
        topiMap.put("179","Science (general)\\International Conferences and Symposiums");
        topiMap.put("180","Science (general)\\Science of Science");
        topiMap.put("181","Science (general)\\Scientific-popular");
        topiMap.put("182","Science (general)\\Scientific and popular: Journalism");
        topiMap.put("183","Education");
        topiMap.put("184","Education\\Theses abstracts");
        topiMap.put("185","Education\\International Conferences and Symposiums");
        topiMap.put("186","Education\\self-help books");
        topiMap.put("187","Education\\Elementary");
        topiMap.put("188","Education\\Encyclopedia");
        topiMap.put("189","Other Social Sciences");
        topiMap.put("190","Other Social Sciences\\Journalism, Media");
        topiMap.put("191","Other Social Sciences\\Cultural");
        topiMap.put("192","Other Social Sciences\\Politics");
        topiMap.put("193","Other Social Sciences\\Politics: International Relations");
        topiMap.put("194","Other Social Sciences\\Sociology");
        topiMap.put("195","Other Social Sciences\\Philosophy");
        topiMap.put("196","Other Social Sciences\\Philosophy: Critical Thinking");
        topiMap.put("197","Other Social Sciences\\Ethnography");
        topiMap.put("198","Psychology");
        topiMap.put("199","Psychology\\Hypnosis");
        topiMap.put("200","Psychology\\The art of communication");
        topiMap.put("201","Psychology\\Love, erotic");
        topiMap.put("202","Psychology\\Neuro-Linguistic Programming");
        topiMap.put("203","Psychology\\Pedagogy");
        topiMap.put("204","Psychology\\Creative Thinking");
        topiMap.put("205","Religion");
        topiMap.put("206","Religion\\Buddhism");
        topiMap.put("207","Religion\\kabbalah");
        topiMap.put("208","Religion\\Orthodoxy");
        topiMap.put("209","Religion\\Esoteric, Mystery");
        topiMap.put("210","Technique");
        topiMap.put("211","Technique\\Automation");
        topiMap.put("212","Technique\\Aerospace Equipment");
        topiMap.put("213","Technique\\Water Treatment");
        topiMap.put("214","Technique\\Military equipment");
        topiMap.put("215","Technique\\Military equipment: Weapon");
        topiMap.put("216","Technique\\Publishing");
        topiMap.put("217","Technology\\Space Science");
        topiMap.put("218","Technique\\Light Industry");
        topiMap.put("219","Technique\\Materials");
        topiMap.put("220","Technology\\Mechanical Engineering");
        topiMap.put("221","Technique\\Metallurgy");
        topiMap.put("222","Technique\\Metrology");
        topiMap.put("223","Technique\\Safety and Security");
        topiMap.put("224","Technique\\Nanotechnology");
        topiMap.put("225","Technique\\Oil and Gas Technologies");
        topiMap.put("226","Technique\\Oil and Gas Technologies: Pipelines");
        topiMap.put("227","Technique\\Regulatory Literature");
        topiMap.put("228","Technique\\Patent Business. Ingenuity. Innovation");
        topiMap.put("229","Technique\\Food Manufacturing");
        topiMap.put("230","Technique\\Instrument");
        topiMap.put("231","Technique\\Industry: Metallurgy");
        topiMap.put("232","Technique\\industrial equipment and technology");
        topiMap.put("233","Technique\\Missiles");
        topiMap.put("234","Technique\\Communication");
        topiMap.put("235","Technique\\Communication: Telecommunications");
        topiMap.put("236","Technique\\Construction");
        topiMap.put("238","Technique\\Construction: Ventilation and Air Conditioning");
        topiMap.put("239","Technique\\Construction: Renovation and interior design");
        topiMap.put("240","Technique\\Construction: Renovation and interior design: Saunas");
        topiMap.put("241","Technique\\Construction: Cement Industry");
        topiMap.put("242","Technique\\Heat");
        topiMap.put("243","Technique\\Fuel Technology");
        topiMap.put("244","Technique\\Transport");
        topiMap.put("245","Technique\\Transportation: Aviation");
        topiMap.put("246","Technique\\Transportation: Cars, motorcycles");
        topiMap.put("247","Technique\\Transportation: Rail");
        topiMap.put("248","Technique\\Transportation: Ships");
        topiMap.put("249","Technique\\Refrigeration");
        topiMap.put("250","Technique\\Electronics");
        topiMap.put("251","Technique\\Electronics: Hardware");
        topiMap.put("252","Technique\\Electronics: Fiber Optics");
        topiMap.put("253","Technique\\Electronics: Home Electronics");
        topiMap.put("254","Technique\\Electronics: Microprocessor Technology");
        topiMap.put("255","Technique\\Electronics: Signal Processing");
        topiMap.put("256","Technique\\Electronics: Radio");
        topiMap.put("257","Technique\\Electronics: Robotics");
        topiMap.put("258","Technique\\Electronics: VLSI");
        topiMap.put("259","Technique\\Electronics: TV. Video");
        topiMap.put("260","Technique\\Electronics: Telecommunications");
        topiMap.put("261","Technique\\Electronics: Electronics");
        topiMap.put("262","Technique\\Energy");
        topiMap.put("263","Technique\\Energy: Renewable Energy");
        topiMap.put("264","Physics");
        topiMap.put("265","Physics\\Astronomy");
        topiMap.put("266","Physics\\Astronomy: Astrophysics");
        topiMap.put("267","Physics\\Geophysics");
        topiMap.put("268","Physics\\Quantum Mechanics");
        topiMap.put("269","Physics\\Quantum Physics");
        topiMap.put("270","Physics\\Crystal Physics");
        topiMap.put("271","Physics\\Mechanics");
        topiMap.put("272","Physics\\Mechanics: Oscillations and Waves");
        topiMap.put("273","Physics\\Mechanics: Mechanics of deformable bodies");
        topiMap.put("274","Physics\\Mechanics: Fluid Mechanics");
        topiMap.put("275","Physics\\Mechanics: Nonlinear dynamics and chaos");
        topiMap.put("276","Physics\\Mechanics: Strength of Materials");
        topiMap.put("277","Physics\\Mechanics: Theory of Elasticity");
        topiMap.put("278","Physics\\General courses");
        topiMap.put("279","Physics\\Optics");
        topiMap.put("280","Physics\\Spectroscopy");
        topiMap.put("281","Physics\\Theory of Relativity and Gravitation");
        topiMap.put("282","Physics\\Thermodynamics and Statistical Mechanics");
        topiMap.put("283","Physics\\Physics of the Atmosphere");
        topiMap.put("284","Physics\\Physics of lasers");
        topiMap.put("285","Physics\\Plasma Physics");
        topiMap.put("286","Physics\\Solid State Physics");
        topiMap.put("287","Physics\\Electricity and Magnetism");
        topiMap.put("288","Physics\\Electrodynamics");
        topiMap.put("289","Physical Education and Sport");
        topiMap.put("290","Physical education and sport\\Bodybuilding");
        topiMap.put("291","Physical education and sport\\Martial Arts");
        topiMap.put("292","Physical education and sport\\Bike");
        topiMap.put("293","Physical education and sport\\Survival");
        topiMap.put("294","Physical Education and Sport\\Sport fishing");
        topiMap.put("295","Physical education and sport\\Fencing");
        topiMap.put("296","Chemistry");
        topiMap.put("297","Chemistry\\Analytical Chemistry");
        topiMap.put("298","Chemistry\\Materials");
        topiMap.put("299","Chemistry\\Inorganic Chemistry");
        topiMap.put("300","Chemistry\\Organic Chemistry");
        topiMap.put("301","Chemistry\\Pyrotechnics and explosives");
        topiMap.put("302","Chemistry\\Pharmacology");
        topiMap.put("303","Chemistry\\Physical Chemistry");
        topiMap.put("304","Chemistry\\Chemical");
        topiMap.put("305","Economy");
        topiMap.put("306","Economy\\Investing");
        topiMap.put("307","Economy\\Mathematical Economics");
        topiMap.put("308","Economy\\Popular");
        topiMap.put("309","Economy\\Markets");
        topiMap.put("310","Economy\\Econometrics");
        topiMap.put("311","Jurisprudence\\Criminology, Forensic Science");
        topiMap.put("312","Jurisprudence\\Criminology: Court. examination");
        topiMap.put("313","Jurisprudence\\Law");
        topiMap.put("314","Linguistics");
        topiMap.put("315","Linguistics\\Foreign");
        topiMap.put("316","Linguistics\\Foreign: English");
        topiMap.put("317","Linguistics\\Foreign: French");
        topiMap.put("318","Linguistics\\Comparative Studies");
        topiMap.put("319","Linguistics\\Linguistics");
        topiMap.put("320","Linguistics\\Rhetoric");
        topiMap.put("321","Linguistics\\Russian Language");
        topiMap.put("322","Linguistics\\Dictionaries");
        topiMap.put("323","Linguistics\\Stylistics");
    return topiMap;
    }

}
