package com.sitemap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedHashMap;

// Java Code To Generate Sitemap

public class SitemapGeneratorWithPagination {

    private static HashSet<String> links;
    private static LinkedHashMap<String, String> crawlCategories;
    private static Writer sitemapOut;
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");

    public SitemapGeneratorWithPagination() {
        links = new HashSet<String>();
        crawlCategories = new LinkedHashMap<>() {{
            //put("common","http://pdfdomain.com/pdf?page=");
/*            put("Business", "http://localhost:8080/pdf/search?q=Business&page=");
            put("Accounting", "http://localhost:8080/pdf/search?q=Accounting&page=");
            put("Logistics", "http://localhost:8080/pdf/search?q=Logistics&page=");
            put("Marketing", "http://localhost:8080/pdf/search?q=Marketing&page=");
            put("Advertising", "http://localhost:8080/pdf/search?q=Advertising&page=");
            put("Management", "http://localhost:8080/pdf/search?q=Management&page=");
            put("Project Management", "http://localhost:8080/pdf/search?q=Project Management&page=");
            put("MLM", "http://localhost:8080/pdf/search?q=MLM&page=");
            put("Responsibility", "http://localhost:8080/pdf/search?q=Responsibility&page=");
            put("Trading", "http://localhost:8080/pdf/search?q=Trading&page=");
            put("Biology", "http://localhost:8080/pdf/search?q=Biology&page=");
            put("Natural Science", "http://localhost:8080/pdf/search?q=Natural Science&page=");
            put("Biostatistics", "http://localhost:8080/pdf/search?q=Biostatistics&page=");
            put("Biotechnology", "http://localhost:8080/pdf/search?q=Biotechnology&page=");
            put("Biophysics", "http://localhost:8080/pdf/search?q=Biophysics&page=");
            put("Anthropology", "http://localhost:8080/pdf/search?q=Anthropology&page=");
            put("Evolution", "http://localhost:8080/pdf/search?q=Evolution&page=");
            put("Biochemistry", "http://localhost:8080/pdf/search?q=Biochemistry&page=");
            put("Enologist", "http://localhost:8080/pdf/search?q=Enologist&page=");
            put("Zoology", "http://localhost:8080/pdf/search?q=Zoology&page=");
            put("Paleontology", "http://localhost:8080/pdf/search?q=Paleontology&page=");
            put("Fish", "http://localhost:8080/pdf/search?q=Fish&page=");
            put("Molecular", "http://localhost:8080/pdf/search?q=Molecular&page=");
            put("Bioinformatics", "http://localhost:8080/pdf/search?q=Bioinformatics&page=");
            put("Plants", "http://localhost:8080/pdf/search?q=Plants&page=");
            put("Botany", "http://localhost:8080/pdf/search?q=Botany&page=");
            put("Agriculture", "http://localhost:8080/pdf/search?q=Agriculture&page=");
            put("Virology", "http://localhost:8080/pdf/search?q=Virology&page=");
            put("Genetics", "http://localhost:8080/pdf/search?q=Genetics&page=");
            put("Microbiology", "http://localhost:8080/pdf/search?q=Microbiology&page=");
            put("Ecology", "http://localhost:8080/pdf/search?q=Ecology&page=");
            put("Art", "http://localhost:8080/pdf/search?q=Art&page=");
            put("Design", "http://localhost:8080/pdf/search?q=Design&page=");
            put("Graphic Arts", "http://localhost:8080/pdf/search?q=Graphic Arts&page=");
            put("Cinema", "http://localhost:8080/pdf/search?q=Cinema&page=");
            put("Music", "http://localhost:8080/pdf/search?q=Music&page=");
            put("Guitar", "http://localhost:8080/pdf/search?q=Guitar&page=");
            put("Photo", "http://localhost:8080/pdf/search?q=Photo&page=");
            put("Chemistry", "http://localhost:8080/pdf/search?q=Chemistry&page=");*/
            put("Analytical Chemistry", "http://localhost:8080/pdf/search?q=Analytical Chemistry&page=");
            put("Materials", "http://localhost:8080/pdf/search?q=Materials&page=");
            put("Inorganic Chemistry", "http://localhost:8080/pdf/search?q=Inorganic Chemistry&page=");
            put("Organic Chemistry", "http://localhost:8080/pdf/search?q=Organic Chemistry&page=");
            put("Pyrotechnics", "http://localhost:8080/pdf/search?q=Pyrotechnics&page=");
            put("Pharmacology", "http://localhost:8080/pdf/search?q=Pharmacology&page=");
            put("Physical Chemistry", "http://localhost:8080/pdf/search?q=Physical Chemistry&page=");
            put("Chemical", "http://localhost:8080/pdf/search?q=Chemical&page=");
            put("Economy", "http://localhost:8080/pdf/search?q=Economy&page=");
            put("Investing", "http://localhost:8080/pdf/search?q=Investing&page=");
            put("Mathematical Economics", "http://localhost:8080/pdf/search?q=Mathematical Economics&page=");
            put("Popular", "http://localhost:8080/pdf/search?q=Popular&page=");
            put("Markets", "http://localhost:8080/pdf/search?q=Markets&page=");
            put("Econometrics", "http://localhost:8080/pdf/search?q=Econometrics&page=");
            put("Criminology", "http://localhost:8080/pdf/search?q=Criminology&page=");
            put("Jurisprudence", "http://localhost:8080/pdf/search?q=Jurisprudence&page=");
            put("Court Examination", "http://localhost:8080/pdf/search?q=Court Examination&page=");
            put("Law", "http://localhost:8080/pdf/search?q=Law&page=");
            put("Linguistics", "http://localhost:8080/pdf/search?q=Linguistics&page=");
            put("Foreign", "http://localhost:8080/pdf/search?q=Foreign&page=");
            put("Foreign", "http://localhost:8080/pdf/search?q=Foreign&page=");
            put("Foreign", "http://localhost:8080/pdf/search?q=Foreign&page=");
            put("Comparative Studies", "http://localhost:8080/pdf/search?q=Comparative Studies&page=");
            put("Rhetoric", "http://localhost:8080/pdf/search?q=Rhetoric&page=");
            put("Russian", "http://localhost:8080/pdf/search?q=Russian&page=");
            put("Dictionaries", "http://localhost:8080/pdf/search?q=Dictionaries&page=");
            put("Stylistics", "http://localhost:8080/pdf/search?q=Stylistics&page=");
            put("History", "http://localhost:8080/pdf/search?q=History&page=");
            put("American Studies", "http://localhost:8080/pdf/search?q=American Studies&page=");
            put("Archaeology", "http://localhost:8080/pdf/search?q=Archaeology&page=");
            put("Military History", "http://localhost:8080/pdf/search?q=Military History&page=");
            put("Memoirs", "http://localhost:8080/pdf/search?q=Memoirs&page=");
            put("Biographies", "http://localhost:8080/pdf/search?q=Biographies&page=");
            put("Literature", "http://localhost:8080/pdf/search?q=Literature&page=");
            put("Fiction", "http://localhost:8080/pdf/search?q=Fiction&page=");
            put("Library", "http://localhost:8080/pdf/search?q=Library&page=");
            put("Detective", "http://localhost:8080/pdf/search?q=Detective&page=");
            put("Children", "http://localhost:8080/pdf/search?q=Children&page=");
            put("Comics", "http://localhost:8080/pdf/search?q=Comics&page=");
            put("Literary", "http://localhost:8080/pdf/search?q=Literary&page=");
            put("Poetry", "http://localhost:8080/pdf/search?q=Poetry&page=");
            put("Prose", "http://localhost:8080/pdf/search?q=Prose&page=");
            put("Folklore", "http://localhost:8080/pdf/search?q=Folklore&page=");
            put("Fantasy", "http://localhost:8080/pdf/search?q=Fantasy&page=");
            put("Religion", "http://localhost:8080/pdf/search?q=Religion&page=");
            put("Buddhism", "http://localhost:8080/pdf/search?q=Buddhism&page=");
            put("Kabbalah", "http://localhost:8080/pdf/search?q=Kabbalah&page=");
            put("Orthodoxy", "http://localhost:8080/pdf/search?q=Orthodoxy&page=");
            put("Esoteric", "http://localhost:8080/pdf/search?q=Esoteric&page=");
            put("Technique", "http://localhost:8080/pdf/search?q=Technique&page=");
            put("Automation", "http://localhost:8080/pdf/search?q=Automation&page=");
            put("Aerospace Equipment", "http://localhost:8080/pdf/search?q=Aerospace Equipment&page=");
            put("Water Treatment", "http://localhost:8080/pdf/search?q=Water Treatment&page=");
            put("Military Equipment", "http://localhost:8080/pdf/search?q=Military Equipment&page=");
            put("Military Weapon", "http://localhost:8080/pdf/search?q=Military Weapon&page=");
            put("Publishing", "http://localhost:8080/pdf/search?q=Publishing&page=");
            put("Space Technology", "http://localhost:8080/pdf/search?q=Space Technology&page=");
            put("Light", "http://localhost:8080/pdf/search?q=Light&page=");
            put("Materials", "http://localhost:8080/pdf/search?q=Materials&page=");
            put("Mechanical Engineering", "http://localhost:8080/pdf/search?q=Mechanical Engineering&page=");
            put("Metallurgy", "http://localhost:8080/pdf/search?q=Metallurgy&page=");
            put("Metrology", "http://localhost:8080/pdf/search?q=Metrology&page=");
            put("Safety and Security", "http://localhost:8080/pdf/search?q=Safety and Security&page=");
            put("Nanotechnology", "http://localhost:8080/pdf/search?q=Nanotechnology&page=");
            put("Oil and Gas Technologies", "http://localhost:8080/pdf/search?q=Oil and Gas Technologies&page=");
            put("Oil and Gas Pipelines", "http://localhost:8080/pdf/search?q=Oil and Gas Pipelines&page=");
            put("Regulatory Literature", "http://localhost:8080/pdf/search?q=Regulatory Literature&page=");
            put("Patent Business", "http://localhost:8080/pdf/search?q=Patent Business&page=");
            put("Food Manufacturing", "http://localhost:8080/pdf/search?q=Food Manufacturing&page=");
            put("Instrument", "http://localhost:8080/pdf/search?q=Instrument&page=");
            put("Industrial Equipment and Technology", "http://localhost:8080/pdf/search?q=Industrial Equipment and Technology&page=");
            put("Missiles", "http://localhost:8080/pdf/search?q=Missiles&page=");
            put("Communication", "http://localhost:8080/pdf/search?q=Communication&page=");
            put("Communication Telecommunications", "http://localhost:8080/pdf/search?q=Communication Telecommunications&page=");
            put("Construction", "http://localhost:8080/pdf/search?q=Construction&page=");
            put("Ventilation and Air Conditioning", "http://localhost:8080/pdf/search?q=Ventilation and Air Conditioning&page=");
            put("Renovation and interior design", "http://localhost:8080/pdf/search?q=Renovation and interior design&page=");
            put("Renovation and interior design: Saunas", "http://localhost:8080/pdf/search?q=Renovation and interior design: Saunas&page=");
            put("Cement Industry", "http://localhost:8080/pdf/search?q=Cement Industry&page=");
            put("Heat Technology", "http://localhost:8080/pdf/search?q=Heat Technology&page=");
            put("Fuel Technology", "http://localhost:8080/pdf/search?q=Fuel Technology&page=");
            put("Transport", "http://localhost:8080/pdf/search?q=Transport&page=");
            put("Transportation Aviation", "http://localhost:8080/pdf/search?q=Transportation Aviation&page=");
            put("Transportation Cars, motorcycles", "http://localhost:8080/pdf/search?q=Transportation Cars, motorcycles&page=");
            put("Transportation Rail", "http://localhost:8080/pdf/search?q=Transportation Rail&page=");
            put("Transportation Ships", "http://localhost:8080/pdf/search?q=Transportation Ships&page=");
            put("Refrigeration", "http://localhost:8080/pdf/search?q=Refrigeration&page=");
            put("Electronics", "http://localhost:8080/pdf/search?q=Electronics&page=");
            put("Electronics Hardware", "http://localhost:8080/pdf/search?q=Electronics Hardware&page=");
            put("Electronics Fiber Optics", "http://localhost:8080/pdf/search?q=Electronics Fiber Optics&page=");
            put("Electronics Home Electronics", "http://localhost:8080/pdf/search?q=Electronics Home Electronics&page=");
            put("Electronics Microprocessor Technology", "http://localhost:8080/pdf/search?q=Electronics Microprocessor Technology&page=");
            put("Electronics Signal Processing", "http://localhost:8080/pdf/search?q=Electronics Signal Processing&page=");
            put("Electronics Radio", "http://localhost:8080/pdf/search?q=Electronics Radio&page=");
            put("Electronics Robotics", "http://localhost:8080/pdf/search?q=Electronics Robotics&page=");
            put("Electronics VLSI", "http://localhost:8080/pdf/search?q=Electronics VLSI&page=");
            put("Electronics TV, Video", "http://localhost:8080/pdf/search?q=Electronics TV, Video&page=");
            put("Electronics Telecommunications", "http://localhost:8080/pdf/search?q=Electronics Telecommunications&page=");
            put("Electronics Electronics", "http://localhost:8080/pdf/search?q=Electronics Electronics&page=");
            put("Energy", "http://localhost:8080/pdf/search?q=Energy&page=");
            put("Energy Renewable Energy", "http://localhost:8080/pdf/search?q=Energy Renewable Energy&page=");
            put("Psychology", "http://localhost:8080/pdf/search?q=Psychology&page=");
            put("Hypnosis", "http://localhost:8080/pdf/search?q=Hypnosis&page=");
            put("Communication", "http://localhost:8080/pdf/search?q=Communication&page=");
            put("Love", "http://localhost:8080/pdf/search?q=Love&page=");
            put("Erotic", "http://localhost:8080/pdf/search?q=Erotic&page=");
            put("Pedagogy", "http://localhost:8080/pdf/search?q=Pedagogy&page=");
            put("Creative", "http://localhost:8080/pdf/search?q=Creative&page=");
            put("Neuro-Linguistic Programming", "http://localhost:8080/pdf/search?q=Neuro-Linguistic Programming&page=");
            put("Social", "http://localhost:8080/pdf/search?q=Social&page=");
            put("Journalism", "http://localhost:8080/pdf/search?q=Journalism&page=");
            put("Media", "http://localhost:8080/pdf/search?q=Media&page=");
            put("Cultural", "http://localhost:8080/pdf/search?q=Cultural&page=");
            put("Politics", "http://localhost:8080/pdf/search?q=Politics&page=");
            put("International Relations", "http://localhost:8080/pdf/search?q=International Relations&page=");
            put("Sociology", "http://localhost:8080/pdf/search?q=Sociology&page=");
            put("Philosophy", "http://localhost:8080/pdf/search?q=Philosophy&page=");
            put("Ethnography", "http://localhost:8080/pdf/search?q=Ethnography&page=");
            put("Critical Thinking", "http://localhost:8080/pdf/search?q=Critical Thinking&page=");
            put("Housekeeping", "http://localhost:8080/pdf/search?q=Housekeeping&page=");
            put("Leisure", "http://localhost:8080/pdf/search?q=Leisure&page=");
            put("Aquaria", "http://localhost:8080/pdf/search?q=Aquaria&page=");
            put("Astrology", "http://localhost:8080/pdf/search?q=Astrology&page=");
            put("Pet", "http://localhost:8080/pdf/search?q=Pet&page=");
            put("Collecting", "http://localhost:8080/pdf/search?q=Collecting&page=");
            put("Beauty", "http://localhost:8080/pdf/search?q=Beauty&page=");
            put("Cooking", "http://localhost:8080/pdf/search?q=Cooking&page=");
            put("Fashion", "http://localhost:8080/pdf/search?q=Fashion&page=");
            put("Jewelry", "http://localhost:8080/pdf/search?q=Jewelry&page=");
            put("Homebrew", "http://localhost:8080/pdf/search?q=Homebrew&page=");
            put("Professions Trades", "http://localhost:8080/pdf/search?q=Professions Trades&page=");
            put("Garden", "http://localhost:8080/pdf/search?q=Garden&page=");
            put("Handicraft", "http://localhost:8080/pdf/search?q=Handicraft&page=");
            put("Hunting", "http://localhost:8080/pdf/search?q=Hunting&page=");
            put("Handicraft", "http://localhost:8080/pdf/search?q=Handicraft&page=");
            put("Cutting", "http://localhost:8080/pdf/search?q=Cutting&page=");
            put("Game Management", "http://localhost:8080/pdf/search?q=Game Management&page=");
            put("Board Games", "http://localhost:8080/pdf/search?q=Board Games&page=");
            put("Chess", "http://localhost:8080/pdf/search?q=Chess&page=");
            put("Physics", "http://localhost:8080/pdf/search?q=Physics&page=");
            put("Astronomy", "http://localhost:8080/pdf/search?q=Astronomy&page=");
            put("Astronomy Astrophysics", "http://localhost:8080/pdf/search?q=Astronomy Astrophysics&page=");
            put("Geophysics", "http://localhost:8080/pdf/search?q=Geophysics&page=");
            put("Quantum Mechanics", "http://localhost:8080/pdf/search?q=Quantum Mechanics&page=");
            put("Quantum Physics", "http://localhost:8080/pdf/search?q=Quantum Physics&page=");
            put("Crystal Physics", "http://localhost:8080/pdf/search?q=Crystal Physics&page=");
            put("Mechanics", "http://localhost:8080/pdf/search?q=Mechanics&page=");
            put("Mechanics Oscillations and Waves", "http://localhost:8080/pdf/search?q=Mechanics Oscillations and Waves&page=");
            put("Mechanics  Mechanics of deformable bodies", "http://localhost:8080/pdf/search?q=Mechanics  Mechanics of deformable bodies&page=");
            put("Mechanics Fluid Mechanics", "http://localhost:8080/pdf/search?q=Mechanics Fluid Mechanics&page=");
            put("Mechanics Nonlinear dynamics and chaos", "http://localhost:8080/pdf/search?q=Mechanics Nonlinear dynamics and chaos&page=");
            put("Mechanics Strength of Materials", "http://localhost:8080/pdf/search?q=Mechanics Strength of Materials&page=");
            put("Mechanics Theory of Elasticity", "http://localhost:8080/pdf/search?q=Mechanics Theory of Elasticity&page=");
            put("General courses", "http://localhost:8080/pdf/search?q=General courses&page=");
            put("Optics", "http://localhost:8080/pdf/search?q=Optics&page=");
            put("Spectroscopy", "http://localhost:8080/pdf/search?q=Spectroscopy&page=");
            put("Theory of Relativity and Gravitation", "http://localhost:8080/pdf/search?q=Theory of Relativity and Gravitation&page=");
            put("Thermodynamics and Statistical Mechanics", "http://localhost:8080/pdf/search?q=Thermodynamics and Statistical Mechanics&page=");
            put("Physics of the Atmosphere", "http://localhost:8080/pdf/search?q=Physics of the Atmosphere&page=");
            put("Physics of lasers", "http://localhost:8080/pdf/search?q=Physics of lasers&page=");
            put("Plasma Physics", "http://localhost:8080/pdf/search?q=Plasma Physics&page=");
            put("Solid State Physics", "http://localhost:8080/pdf/search?q=Solid State Physics&page=");
            put("Electricity and Magnetism", "http://localhost:8080/pdf/search?q=Electricity and Magnetism&page=");
            put("Electrodynamics", "http://localhost:8080/pdf/search?q=Electrodynamics&page=");
            put("Education", "http://localhost:8080/pdf/search?q=Education&page=");
            put("Theses", "http://localhost:8080/pdf/search?q=Theses&page=");
            put("Conferences", "http://localhost:8080/pdf/search?q=Conferences&page=");
            put("Self", "http://localhost:8080/pdf/search?q=Self&page=");
            put("Elementary", "http://localhost:8080/pdf/search?q=Elementary&page=");
            put("Encyclopedia", "http://localhost:8080/pdf/search?q=Encyclopedia&page=");
            put("Mathematics", "http://localhost:8080/pdf/search?q=Mathematics&page=");
            put("Algebra", "http://localhost:8080/pdf/search?q=Algebra&page=");
            put("Linear Algebra", "http://localhost:8080/pdf/search?q=Linear Algebra&page=");
            put("Algorithms", "http://localhost:8080/pdf/search?q=Algorithms&page=");
            put("Data Structures", "http://localhost:8080/pdf/search?q=Data Structures&page=");
            put("Analysis", "http://localhost:8080/pdf/search?q=Analysis&page=");
            put("Wavelets Signal processing", "http://localhost:8080/pdf/search?q=Wavelets Signal processing&page=");
            put("Probability", "http://localhost:8080/pdf/search?q=Probability&page=");
            put("Computational Mathematics", "http://localhost:8080/pdf/search?q=Computational Mathematics&page=");
            put("Geometry and Topology", "http://localhost:8080/pdf/search?q=Geometry and Topology&page=");
            put("Puzzle", "http://localhost:8080/pdf/search?q=Puzzle&page=");
            put("Dynamical Systems", "http://localhost:8080/pdf/search?q=Dynamical Systems&page=");
            put("Discrete Mathematics", "http://localhost:8080/pdf/search?q=Discrete Mathematics&page=");
            put("Differential Equations", "http://localhost:8080/pdf/search?q=Differential Equations&page=");
            put("Combinatorics", "http://localhost:8080/pdf/search?q=Combinatorics&page=");
            put("Complex Variable", "http://localhost:8080/pdf/search?q=Complex Variable&page=");//Downloaded till here
            put("Computer Algebra", "http://localhost:8080/pdf/search?q=Computer Algebra&page=");
            put("Lectures", "http://localhost:8080/pdf/search?q=Lectures&page=");
            put("Logic", "http://localhost:8080/pdf/search?q=Logic&page=");
            put("Mathematical Statistics", "http://localhost:8080/pdf/search?q=Mathematical Statistics&page=");
            put("Mathematical Physics", "http://localhost:8080/pdf/search?q=Mathematical Physics&page=");
            put("Continued Fractions", "http://localhost:8080/pdf/search?q=Continued Fractions&page=");
            put("Fuzzy Logic and Applications", "http://localhost:8080/pdf/search?q=Fuzzy Logic and Applications&page=");
            put("Optimal Control", "http://localhost:8080/pdf/search?q=Optimal Control&page=");
            put("Operations  Research", "http://localhost:8080/pdf/search?q=Operations  Research&page=");
            put("Applied Mathematics", "http://localhost:8080/pdf/search?q=Applied Mathematics&page=");
            put("Symmetry and Group", "http://localhost:8080/pdf/search?q=Symmetry and Group&page=");
            put("Automatic Control Theory", "http://localhost:8080/pdf/search?q=Automatic Control Theory&page=");
            put("Graph Theory", "http://localhost:8080/pdf/search?q=Graph Theory&page=");
            put("Game Theory", "http://localhost:8080/pdf/search?q=Game Theory&page=");
            put("Operator Theory", "http://localhost:8080/pdf/search?q=Operator Theory&page=");
            put("Number Theory", "http://localhost:8080/pdf/search?q=Number Theory&page=");
            put("Functional Analysis", "http://localhost:8080/pdf/search?q=Functional Analysis&page=");
            put("Numerical Analysis", "http://localhost:8080/pdf/search?q=Numerical Analysis&page=");
            put("Elementary", "http://localhost:8080/pdf/search?q=Elementary&page=");
            put("Science", "http://localhost:8080/pdf/search?q=Science&page=");
            put("General", "http://localhost:8080/pdf/search?q=General&page=");
            put("Conferences", "http://localhost:8080/pdf/search?q=Conferences&page=");
            put("Symposiums", "http://localhost:8080/pdf/search?q=Symposiums&page=");
            put("Journalism", "http://localhost:8080/pdf/search?q=Journalism&page=");
            put("Popular", "http://localhost:8080/pdf/search?q=Popular&page=");
            put("Medicine", "http://localhost:8080/pdf/search?q=Medicine&page=");
            put("Anatomy", "http://localhost:8080/pdf/search?q=Anatomy&page=");
            put("Physiology", "http://localhost:8080/pdf/search?q=Physiology&page=");
            put("Anesthesiology", "http://localhost:8080/pdf/search?q=Anesthesiology&page=");
            put("Intensive Care", "http://localhost:8080/pdf/search?q=Intensive Care&page=");
            put("Diseases", "http://localhost:8080/pdf/search?q=Diseases&page=");
            put("Internal", "http://localhost:8080/pdf/search?q=Internal&page=");
            put("Histology", "http://localhost:8080/pdf/search?q=Histology&page=");
            put("Homeopathy", "http://localhost:8080/pdf/search?q=Homeopathy&page=");
            put("Dermatology", "http://localhost:8080/pdf/search?q=Dermatology&page=");
            put("Diabetes", "http://localhost:8080/pdf/search?q=Diabetes&page=");
            put("Immunology", "http://localhost:8080/pdf/search?q=Immunology&page=");
            put("Infectious Diseases", "http://localhost:8080/pdf/search?q=Infectious Diseases&page=");
            put("Yoga", "http://localhost:8080/pdf/search?q=Yoga&page=");
            put("Cardiology", "http://localhost:8080/pdf/search?q=Cardiology&page=");
            put("Chinese Medicine", "http://localhost:8080/pdf/search?q=Chinese Medicine&page=");
            put("Clinical Medicine", "http://localhost:8080/pdf/search?q=Clinical Medicine&page=");
            put("Molecular Medicine", "http://localhost:8080/pdf/search?q=Molecular Medicine&page=");
            put("Natural Medicine", "http://localhost:8080/pdf/search?q=Natural Medicine&page=");
            put("Scientific Literature", "http://localhost:8080/pdf/search?q=Scientific Literature&page=");
            put("Neurology", "http://localhost:8080/pdf/search?q=Neurology&page=");
            put("Oncology", "http://localhost:8080/pdf/search?q=Oncology&page=");
            put("ENT", "http://localhost:8080/pdf/search?q=ENT&page=");
            put("Ophthalmology", "http://localhost:8080/pdf/search?q=Ophthalmology&page=");
            put("Pediatrics", "http://localhost:8080/pdf/search?q=Pediatrics&page=");
            put("Dentistry", "http://localhost:8080/pdf/search?q=Dentistry&page=");
            put("Orthodontics", "http://localhost:8080/pdf/search?q=Orthodontics&page=");
            put("Trial", "http://localhost:8080/pdf/search?q=Trial&page=");
            put("Therapy", "http://localhost:8080/pdf/search?q=Therapy&page=");
            put("Pharmacology", "http://localhost:8080/pdf/search?q=Pharmacology&page=");
            put("Feng Shui", "http://localhost:8080/pdf/search?q=Feng Shui&page=");
            put("Surgery", "http://localhost:8080/pdf/search?q=Surgery&page=");
            put("Orthopedics", "http://localhost:8080/pdf/search?q=Orthopedics&page=");
            put("Endocrinology", "http://localhost:8080/pdf/search?q=Endocrinology&page=");
            put("Epidemiology", "http://localhost:8080/pdf/search?q=Epidemiology&page=");
            put("Geography", "http://localhost:8080/pdf/search?q=Geography&page=");
            put("Geodesy", "http://localhost:8080/pdf/search?q=Geodesy&page=");
            put("Cartography", "http://localhost:8080/pdf/search?q=Cartography&page=");
            put("Local History", "http://localhost:8080/pdf/search?q=Local History&page=");
            put("Tourism", "http://localhost:8080/pdf/search?q=Tourism&page=");
            put("Geology", "http://localhost:8080/pdf/search?q=Geology&page=");
            put("Hydrogeology", "http://localhost:8080/pdf/search?q=Hydrogeology&page=");
            put("Mining", "http://localhost:8080/pdf/search?q=Mining&page=");
            put("Computers", "http://localhost:8080/pdf/search?q=Computers&page=");
            put("Web", "http://localhost:8080/pdf/search?q=Web&page=");
            put("Databases", "http://localhost:8080/pdf/search?q=Databases&page=");
            put("Security", "http://localhost:8080/pdf/search?q=Security&page=");
            put("Cryptography", "http://localhost:8080/pdf/search?q=Cryptography&page=");
            put("Algorithms", "http://localhost:8080/pdf/search?q=Algorithms&page=");
            put("Cryptography", "http://localhost:8080/pdf/search?q=Cryptography&page=");
            put("Image Processing", "http://localhost:8080/pdf/search?q=Image Processing&page=");
            put("Pattern Recognition", "http://localhost:8080/pdf/search?q=Pattern Recognition&page=");
            put("Digital Watermarks", "http://localhost:8080/pdf/search?q=Digital Watermarks&page=");
            put("Cybernetics", "http://localhost:8080/pdf/search?q=Cybernetics&page=");
            put("Artificial Intelligence", "http://localhost:8080/pdf/search?q=Artificial Intelligence&page=");
            put("Information Systems", "http://localhost:8080/pdf/search?q=Information Systems&page=");
            put("EC Businesses", "http://localhost:8080/pdf/search?q=EC Businesses&page=");
            put("Programming", "http://localhost:8080/pdf/search?q=Programming&page=");
            put("Libraries API", "http://localhost:8080/pdf/search?q=Libraries API&page=");
            put("Games", "http://localhost:8080/pdf/search?q=Games&page=");
            put("Compilers", "http://localhost:8080/pdf/search?q=Compilers&page=");
            put("Modeling Languages", "http://localhost:8080/pdf/search?q=Modeling Languages&page=");
            put("Programming Languages", "http://localhost:8080/pdf/search?q=Programming Languages&page=");
            put("Software", "http://localhost:8080/pdf/search?q=Software&page=");
            put("Office Software", "http://localhost:8080/pdf/search?q=Office Software&page=");
            put("Adobe Products", "http://localhost:8080/pdf/search?q=Adobe Products&page=");
            put("Macromedia Products", "http://localhost:8080/pdf/search?q=Macromedia Products&page=");
            put("CAD", "http://localhost:8080/pdf/search?q=CAD&page=");
            put("Systems Scientific Computing", "http://localhost:8080/pdf/search?q=Systems Scientific Computing&page=");
            put("Networking", "http://localhost:8080/pdf/search?q=Networking&page=");
            put("Internet", "http://localhost:8080/pdf/search?q=Internet&page=");
            put("Lectures", "http://localhost:8080/pdf/search?q=Lectures&page=");
            put("Media", "http://localhost:8080/pdf/search?q=Media&page=");
            put("Operating Systems", "http://localhost:8080/pdf/search?q=Operating Systems&page=");
            put("Data Processing", "http://localhost:8080/pdf/search?q=Data Processing&page=");
            put("System Administration", "http://localhost:8080/pdf/search?q=System Administration&page=");
            put("method", "http://localhost:8080/pdf/search?q=method&page=");
            put("Educational", "http://localhost:8080/pdf/search?q=Educational&page=");
            put("Music", "http://localhost:8080/pdf/search?q=Music&page=");
            put("Fiction", "http://localhost:8080/pdf/search?q=Fiction&page=");
            put("Science", "http://localhost:8080/pdf/search?q=Science&page=");
            put("Fashion", "http://localhost:8080/pdf/search?q=Fashion&page=");
            put("Photography", "http://localhost:8080/pdf/search?q=Photography&page=");
            put("Travel", "http://localhost:8080/pdf/search?q=Travel&page=");
            put("Finance", "http://localhost:8080/pdf/search?q=Finance&page=");
            put("Personality", "http://localhost:8080/pdf/search?q=Personality&page=");
            put("Education", "http://localhost:8080/pdf/search?q=Education&page=");
            put("History", "http://localhost:8080/pdf/search?q=History&page=");
            put("Software", "http://localhost:8080/pdf/search?q=Software&page=");
            put("Business", "http://localhost:8080/pdf/search?q=Business&page=");
            put("Engineering", "http://localhost:8080/pdf/search?q=Engineering&page=");
            put("Sports", "http://localhost:8080/pdf/search?q=Sports&page=");
            put("Cooking", "http://localhost:8080/pdf/search?q=Cooking&page=");
            put("Management", "http://localhost:8080/pdf/search?q=Management&page=");
        }};
    }

    public void getPageLinks(String URL1) throws IOException {

        for(String urlCategory: crawlCategories.keySet()) {
            if(links.size() > 75000)
            {
                links = new HashSet<String>();
            }
            File sitemapFile = new File("sitemap-" + urlCategory + ".xml");
            sitemapOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sitemapFile), "UTF8"));
            sitemapOut.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("\r\n");
            sitemapOut.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.sitemaps.org/schemas/sitemap/0.9 http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd\">").append("\r\n").append("\t");
            sitemapOut.flush();
            for (int i = 1; i < 200; i++) {
                String URL = crawlCategories.get(urlCategory);
                System.out.println("URL "+URL);
                String newURL = urlify(URL) + i;
                System.out.println("new "+newURL);
                try {
                    Thread.sleep(3000L);
                    Document document = Jsoup.connect(newURL).get();
                    //3. Parse the HTML to extract links to other URLs
                    Elements linksOnPage = document.select("a[href]");
                    if(linksOnPage.size() < 150)
                    {
                        System.out.println("############ Breaking at "+newURL);
                        break;
                    }
                    //5. For each extracted URL... go back to Step 4.
                    for (Element page : linksOnPage) {
                        String uri = page.attr("abs:href");
                        uri = urlify(uri);
                        String uriAmp = uri.replaceAll("&","&amp;");
                        if (!links.contains(uri)) {
                            sitemapOut.append("<url>").append("\r\n").append("\t").append("\t");
                            sitemapOut.append("<loc>" + uriAmp + "</loc>").append("\r\n").append("\t").append("\t");
                            sitemapOut.append("<lastmod>" + simpleDateFormat.format(Calendar.getInstance().getTime()) + "</lastmod>").append("\r\n").append("\t").append("\t");
                            sitemapOut.append("<changefreq>weekly</changefreq>").append("\r\n").append("\t").append("\t");
                            sitemapOut.append("<priority>1.0</priority>").append("\r\n").append("\t");
                            sitemapOut.append("</url>").append("\r\n").append("\t");
                            links.add(uri);
                            System.out.println("Added " + uri);
                        }
                        sitemapOut.flush();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            sitemapOut.append("</urlset>").append("\r\n");
            sitemapOut.flush();
        }
    }

    public static void main(String[] args) throws IOException {
        new SitemapGeneratorWithPagination().getPageLinks("");
    }

    public static String urlify(String text) {
        //text = text.replaceAll("&amp;","&");
        if (!text.contains(" ")) {
            return text;
        }

        // Use urlifiedText for building the result text
        StringBuilder urlifiedText  = new StringBuilder();

        // Replace spaces with %20, after trimming leading and trailing spaces
        for (char currentChar : text.trim().toCharArray()) {

            if (currentChar == ' ') {
                urlifiedText.append("%20");
            } else {
                urlifiedText.append(currentChar);
            }
        }

        return urlifiedText.toString();
    }
}