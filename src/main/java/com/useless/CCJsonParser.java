package com.useless;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opencsv.CSVWriter;

public class CCJsonParser
{
	private static List<String> badwords = new ArrayList<String>();
	private static Pattern pattern1;
	private static Pattern pattern2;
	private static Pattern pattern3;
	//private static Pattern pattern4;
	static
	{
		fillBadWords();
		String joinedBadWords = StringUtils.join(badwords, "|");
		String patternString1 = "\\b(" + joinedBadWords + ")\\b";
		String patternString2 = "(" + joinedBadWords + ")\\b";
		String patternString3 = "\\b(" + joinedBadWords + ")";
		//String patternString4 = joinedBadWords;
		pattern1 = Pattern.compile(patternString1);
		pattern2 = Pattern.compile(patternString2);
		pattern3 = Pattern.compile(patternString3);
		//pattern4 = Pattern.compile(patternString4);
	}

	public static BufferedReader getBufferedReaderForCompressedFile(String fileIn)
			throws FileNotFoundException, CompressorException
	{
		FileInputStream fin = new FileInputStream(fileIn);
		BufferedInputStream bis = new BufferedInputStream(fin);
		CompressorInputStream input = new CompressorStreamFactory().createCompressorInputStream(bis);
		BufferedReader br2 = new BufferedReader(new InputStreamReader(input));
		return br2;
	}

	private static void fillBadWords()
	{
		badwords.add("2g1c");
		badwords.add("2 girls 1 cup");
		badwords.add("acrotomophilia");
		badwords.add("alabama hot pocket");
		badwords.add("alaskan pipeline");
		badwords.add("anal fuck");
		badwords.add("anilingus");
		badwords.add("animalsex");
		badwords.add("anus");
		badwords.add("apeshit");
		badwords.add("arsehole");
		//badwords.add("ass");
		badwords.add("asshole");
		badwords.add("assmunch");
		badwords.add("auto erotic");
		badwords.add("autoerotic");
		badwords.add("babeland");
		badwords.add("baby batter");
		badwords.add("baby juice");
		badwords.add("ball gag");
		badwords.add("ball gravy");
		badwords.add("ball kicking");
		badwords.add("ball licking");
		badwords.add("ball sack");
		badwords.add("ball sucking");
		badwords.add("bangbros");
		badwords.add("bareback");
		badwords.add("barely legal");
		badwords.add("barenaked");
		badwords.add("bastard");
		badwords.add("bastardo");
		badwords.add("bastinado");
		badwords.add("bbw");
		badwords.add("bdsm");
		badwords.add("beaner");
		badwords.add("beaners");
		badwords.add("beaver cleaver");
		badwords.add("beaver lips");
		badwords.add("bestiality");
		badwords.add("big black");
		badwords.add("big breasts");
		badwords.add("big knockers");
		badwords.add("big tits");
		badwords.add("bimbos");
		badwords.add("birdlock");
		badwords.add("bitch");
		badwords.add("bitches");
		badwords.add("black cock");
		badwords.add("blonde action");
		badwords.add("blonde on blonde action");
		badwords.add("blowjob");
		badwords.add("blow job");
		badwords.add("blow your load");
		badwords.add("blue waffle");
		badwords.add("blumpkin");
		badwords.add("bollocks");
		badwords.add("bondage");
		badwords.add("boner");
		badwords.add("boob");
		badwords.add("boobs");
		badwords.add("booty call");
		badwords.add("brown showers");
		badwords.add("brunette action");
		badwords.add("bukkake");
		badwords.add("bulldyke");
		badwords.add("bullet vibe");
		badwords.add("bullshit");
		badwords.add("bung hole");
		badwords.add("bunghole");
		badwords.add("busty");
		badwords.add("butt");
		badwords.add("buttcheeks");
		badwords.add("butthole");
		badwords.add("camel toe");
		badwords.add("camgirl");
		badwords.add("camslut");
		badwords.add("camwhore");
		badwords.add("carpet muncher");
		badwords.add("carpetmuncher");
		badwords.add("chocolate rosebuds");
		badwords.add("circlejerk");
		badwords.add("cleveland steamer");
		badwords.add("clit");
		badwords.add("clitoris");
		badwords.add("clover clamps");
		badwords.add("clusterfuck");
		badwords.add("cock");
		badwords.add("cocks");
		badwords.add("coprolagnia");
		badwords.add("coprophilia");
		badwords.add("cornhole");
		//badwords.add("coon");
		badwords.add("coons");
		badwords.add("creampie");
		//badwords.add("cum");
		badwords.add("cumming");
		badwords.add("cunnilingus");
		badwords.add("cunt");
		badwords.add("darkie");
		badwords.add("date rape");
		badwords.add("daterape");
		badwords.add("deep fisting");
		badwords.add("deep throat");
		badwords.add("deepthroat");
		badwords.add("dendrophilia");
		badwords.add("dick");
		badwords.add("dildo");
		badwords.add("dingleberry");
		badwords.add("dingleberries");
		badwords.add("dirty pillows");
		badwords.add("dirty sanchez");
		badwords.add("doggie style");
		badwords.add("doggiestyle");
		badwords.add("doggy style");
		badwords.add("doggystyle");
		badwords.add("dog style");
		badwords.add("dolcett");
		//badwords.add("domination");
		badwords.add("dominatrix");
		badwords.add("dommes");
		badwords.add("donkey punch");
		badwords.add("double dong");
		badwords.add("double penetration");
		badwords.add("dp action");
		badwords.add("dry hump");
		badwords.add("dvda");
		badwords.add("eat my ass");
		badwords.add("ecchi");
		badwords.add("ejaculation");
		badwords.add("erotic");
		badwords.add("erotism");
		badwords.add("escort");
		badwords.add("eunuch");
		badwords.add("faggot");
		badwords.add("fecal");
		badwords.add("felch");
		badwords.add("fellatio");
		badwords.add("feltch");
		badwords.add("fetish");
		badwords.add("female squirting");
		badwords.add("femdom");
		badwords.add("figging");
		badwords.add("fingerbang");
		badwords.add("fingering");
		badwords.add("fistfucking");
		badwords.add("fisting");
		badwords.add("foot fetish");
		badwords.add("footjob");
		badwords.add("frotting");
		badwords.add("fuck");
		badwords.add("fuck buttons");
		badwords.add("fuckin");
		badwords.add("fucking");
		badwords.add("fucked");
		badwords.add("fucktards");
		badwords.add("fudge packer");
		badwords.add("fudgepacker");
		badwords.add("futanari");
		badwords.add("gang bang");
		badwords.add("gay sex");
		badwords.add("genitals");
		badwords.add("giant cock");
		badwords.add("girl on");
		badwords.add("girl on top");
		badwords.add("girls gone wild");
		badwords.add("goatcx");
		badwords.add("goatse");
		badwords.add("god damn");
		badwords.add("gokkun");
		badwords.add("golden shower");
		badwords.add("goodpoop");
		badwords.add("goo girl");
		badwords.add("goregasm");
		badwords.add("grope");
		badwords.add("group sex");
		badwords.add("g-spot");
		badwords.add("guro");
		badwords.add("hand job");
		badwords.add("handjob");
		badwords.add("hard core");
		badwords.add("hardcore");
		badwords.add("hentai");
		badwords.add("homoerotic");
		badwords.add("Homosex");		
		badwords.add("honkey");
		badwords.add("hooker");
		badwords.add("hot carl");
		badwords.add("hot chick");
		badwords.add("how to kill");
		badwords.add("how to murder");
		badwords.add("huge fat");
		badwords.add("humping");
		badwords.add("incest");
		badwords.add("intercourse");
		badwords.add("jack off");
		badwords.add("jail bait");
		badwords.add("jailbait");
		badwords.add("jelly donut");
		badwords.add("jerk off");
		badwords.add("jigaboo");
		badwords.add("jiggaboo");
		badwords.add("jiggerboo");
		badwords.add("jizz");
		badwords.add("juggs");
		badwords.add("kike");
		badwords.add("kinbaku");
		badwords.add("kinkster");
		badwords.add("kinky");
		badwords.add("knobbing");
		badwords.add("leather restraint");
		badwords.add("leather straight jacket");
		badwords.add("lemon party");
		badwords.add("lolita");
		badwords.add("lovemaking");
		badwords.add("make me come");
		badwords.add("male squirting");
		badwords.add("masturbate");
		badwords.add("menage a trois");
		badwords.add("milf");
		badwords.add("missionary position");
		badwords.add("motherfucker");
		badwords.add("mommyfuck");
		badwords.add("mound of venus");
		badwords.add("mr hands");
		badwords.add("muff diver");
		badwords.add("muffdiving");
		badwords.add("nambla");
		badwords.add("nawashi");
		//badwords.add("negro");
		badwords.add("neonazi");
		badwords.add("nigga");
		badwords.add("nigger");
		badwords.add("nig nog");
		badwords.add("nimphomania");
		badwords.add("nipple");
		badwords.add("nipples");
		badwords.add("nsfw images");
		badwords.add("nude");
		badwords.add("nudity");
		badwords.add("nympho");
		badwords.add("nymphomania");
		badwords.add("octopussy");
		badwords.add("omorashi");
		badwords.add("one cup two girls");
		badwords.add("one guy one jar");
		badwords.add("orgasm");
		badwords.add("orgy");
		badwords.add("paedophile");
		badwords.add("paki");
		badwords.add("panties");
		badwords.add("panty");
		badwords.add("pedobear");
		badwords.add("pedophile");
		badwords.add("pegging");
		badwords.add("penis");
		badwords.add("phone sex");
		badwords.add("piece of shit");
		badwords.add("pissing");
		badwords.add("piss pig");
		badwords.add("pisspig");
		badwords.add("playboy");
		badwords.add("pleasure chest");
		badwords.add("pole smoker");
		badwords.add("ponyplay");
		badwords.add("poof");
		badwords.add("poon");
		badwords.add("poontang");
		badwords.add("punany");
		badwords.add("poop chute");
		badwords.add("poopchute");
		badwords.add("porn");
		badwords.add("porno");
		badwords.add("pornofilm");
		badwords.add("pornofilms");
		badwords.add("pornography");
		badwords.add("pornstar");		
		badwords.add("prince albert piercing");
		badwords.add("pthc");
		badwords.add("pubes");
		badwords.add("pussy");
		badwords.add("queaf");
		badwords.add("queef");
		badwords.add("quim");
		badwords.add("raghead");
		badwords.add("raging boner");
		badwords.add("rape");
		badwords.add("raping");
		badwords.add("rapist");
		badwords.add("rectum");
		badwords.add("reverse cowgirl");
		badwords.add("rimjob");
		badwords.add("rimming");
		badwords.add("rosy palm");
		badwords.add("rosy palm and her 5 sisters");
		badwords.add("rusty trombone");
		badwords.add("sadism");
		badwords.add("santorum");
		//badwords.add("scat");
		badwords.add("schlong");
		badwords.add("scissoring");
		badwords.add("semen");
		badwords.add("sexual");
		badwords.add("sexo");
		badwords.add("sexstories");
		badwords.add("sexvideos");
		badwords.add("sexpics");
		badwords.add("sexfilm");
		badwords.add("sexfilms");
		badwords.add("sex chat");
		badwords.add("sextube");
		badwords.add("sexshop");
		badwords.add("sexy");
		badwords.add("shaved beaver");
		badwords.add("shaved pussy");
		badwords.add("shemale");
		badwords.add("shibari");
		badwords.add("shit");
		badwords.add("shitblimp");
		badwords.add("shitty");
		badwords.add("shota");
		badwords.add("shrimping");
		badwords.add("skeet");
		badwords.add("slanteye");
		badwords.add("slut");
		//badwords.add("s&m");
		badwords.add("smut");
		badwords.add("snatch");
		badwords.add("snowballing");
		badwords.add("sodomize");
		badwords.add("sodomy");
		badwords.add("spanking");
		badwords.add("spic");
		badwords.add("splooge");
		badwords.add("splooge moose");
		badwords.add("spooge");
		badwords.add("spread legs");
		badwords.add("spunk");
		badwords.add("strap on");
		badwords.add("strapon");
		badwords.add("strappado");
		badwords.add("strip club");
		badwords.add("style doggy");
		badwords.add("suck");
		badwords.add("sucks");
		badwords.add("suicide girls");
		badwords.add("sultry women");
		badwords.add("swastika");
		badwords.add("swinger");
		badwords.add("tainted love");
		badwords.add("taste my");
		badwords.add("tea bagging");
		badwords.add("threesome");
		badwords.add("throating");
		badwords.add("tied up");
		badwords.add("tight white");
		//badwords.add("tit");
		badwords.add("tits");
		badwords.add("titties");
		badwords.add("titty");
		badwords.add("tongue in a");
		badwords.add("topless");
		badwords.add("tosser");
		badwords.add("towelhead");
		badwords.add("tranny");
		badwords.add("tribadism");
		badwords.add("tub girl");
		badwords.add("tubgirl");
		badwords.add("tushy");
		//badwords.add("twat");
		//badwords.add("twink");
		badwords.add("twinkie");
		badwords.add("two girls one cup");
		badwords.add("undressing");
		badwords.add("upskirt");
		badwords.add("urethra play");
		badwords.add("urophilia");
		badwords.add("vagina");
		badwords.add("venus mound");
		badwords.add("vibrator");
		badwords.add("violet wand");
		badwords.add("vorarephilia");
		badwords.add("voyeur");
		badwords.add("videosex");
		badwords.add("vulva");
		badwords.add("wank");
		badwords.add("wetback");
		badwords.add("wet dream");
		badwords.add("white power");
		badwords.add("wrapping men");
		badwords.add("wrinkled starfish");
		badwords.add("xx");
		badwords.add("xxx");
		badwords.add("yaoi");
		badwords.add("yellow showers");
		badwords.add("yiffy");
		badwords.add("zoophilia");

	}

	public static void main(String[] args) throws IOException, ParseException, CompressorException
	{
		CSVWriter ipWriter = new CSVWriter(new FileWriter(System.currentTimeMillis() + "_IP.csv"), '\t');
		// feed in your array (or convert your data to an array)
		String[] ipEntries = "Domain;IP".split(";");
		ipWriter.writeNext(ipEntries);

		CSVWriter smWriter = new CSVWriter(new FileWriter(System.currentTimeMillis() + "_SM.csv"), '\t');
		// feed in your array (or convert your data to an array)
		String[] smEntries = "Domain;URL;FACEBOOK;YOUTUBE;TWITTER;GOOGLEPLUS;LINKEDIN;APPLEITUNES;WINDOWSPHONE".split(";");
		smWriter.writeNext(smEntries);

		CSVWriter titleWriter = new CSVWriter(new FileWriter(System.currentTimeMillis() + "_TTL.csv"), '\t');
		// feed in your array (or convert your data to an array)
		String[] titleEntries = "Domain;Title;Description;Keywords".split(";");
		titleWriter.writeNext(titleEntries);

		/*CSVWriter descWriter = new CSVWriter(new FileWriter(System.currentTimeMillis() + "_DESC.csv"), '\t');
		// feed in your array (or convert your data to an array)
		String[] descEntries = "Domain;Desc".split(";");
		descWriter.writeNext(descEntries);*/

		CSVWriter emailWriter = new CSVWriter(new FileWriter(System.currentTimeMillis() + "_EMAIL.csv"), '\t');
		// feed in your array (or convert your data to an array)
		String[] emailEntries = "URLDomain;URL;URLHash;Email;EmailDomain".split(";");
		emailWriter.writeNext(emailEntries);

		CSVWriter phoneWriter = new CSVWriter(new FileWriter(System.currentTimeMillis() + "_PHONE.csv"), '\t');
		// feed in your array (or convert your data to an array)
		String[] phoneEntries = "Domain;Phone".split(";");
		phoneWriter.writeNext(phoneEntries);

		/*CSVWriter keyWriter = new CSVWriter(new FileWriter(System.currentTimeMillis() + "_KEY.csv"), '\t');
		// feed in your array (or convert your data to an array)
		String[] keyEntries = "Domain;Key".split(";");
		keyWriter.writeNext(keyEntries);*/

		final File folder = new File(args[0]);
		File[] fileEntries = folder.listFiles();

		for (File file1 : fileEntries)
		{
			if (file1.isDirectory())
			{
				continue;
			}
			System.out.println("Process Started for " + file1.getName());
			// System.out.println(file1);
			BufferedReader bfRdr = getBufferedReaderForCompressedFile(file1.getAbsolutePath());
			String nextLine = null;
			while ((nextLine = bfRdr.readLine()) != null)
			{
				JsonObject cbJsonObject = null;
				try
				{
					cbJsonObject = new JsonParser().parse(nextLine).getAsJsonObject();
				} 
				catch (Exception ee)
				{
					//System.out.println("Not a valid JSON "+nextLine);
					//System.out.println(ee.getMessage());
					int leftCurlBrace = nextLine.indexOf("{");
					//System.out.println("leftCurlBrace"+leftCurlBrace);
					if(leftCurlBrace != -1)
					{
						nextLine = nextLine.substring(leftCurlBrace, nextLine.length());
						cbJsonObject = new JsonParser().parse(nextLine).getAsJsonObject();
					}
					
				}
				String nextLineLower = nextLine.toLowerCase();
				Matcher matcher1 = pattern1.matcher(nextLineLower);
				Matcher matcher2 = pattern2.matcher(nextLineLower);
				Matcher matcher3 = pattern3.matcher(nextLineLower);
				//Matcher matcher4 = pattern4.matcher(nextLineLower);
				boolean match1 = matcher1.find();
				boolean match2 = matcher2.find();
				boolean match3 = matcher3.find();
				//boolean match4 = matcher4.find();
				while (match1 || match2 || match3)
				{
					//System.out.println("Match1 - " + match1 + " Match2 - " + match2 + " Match3 - " + match3);
					//System.out.println("Possible obscene word in line - " + cbJsonObject);
					Iterator<String> iter = badwords.listIterator();
					while(iter.hasNext())
					{
						String badword = iter.next();
						if(nextLine.contains(badword))
						{
							//System.out.println("This is the obscene word "+badword);
							//System.out.println();
							//System.out.println();
							cbJsonObject = null;
							break;
						}
					}
					cbJsonObject = null;
					break;
				}

				if (cbJsonObject == null || cbJsonObject.isJsonNull())
				{
					continue;
				}
				String domain = cbJsonObject.get("topDomain").getAsString();
				
				String ipArr[] = new String[]
				{ domain, cbJsonObject.get("IP").getAsString() };
				ipWriter.writeNext(ipArr);
				 
				URI uri = null;
				try
				{
					uri = new URI(cbJsonObject.get("crURL").getAsString());
					String domain1 = uri.getHost();
					String path = uri.getPath();
					if(domain1.equalsIgnoreCase(domain) && (path.length() <= 1) && (uri.getRawQuery() == null))
					{
						writeIntoSocialMediaJSON(domain, cbJsonObject, smWriter);
						writeIntoTitleJSON(domain, cbJsonObject, titleWriter);
						//writeIntoDescJSON(domain, cbJsonObject, descWriter);
						//writeIntoKeyJSON(domain	, cbJsonObject, keyWriter );						
					}
				}
				catch (URISyntaxException e)
				{
					e.printStackTrace();
				}
				writeIntoEmailJSON(domain, cbJsonObject, emailWriter);
				writeIntoPhoneJSON(domain, cbJsonObject, phoneWriter);
			}
			ipWriter.flush();
			titleWriter.flush();
			//descWriter.flush();
			emailWriter.flush();
			phoneWriter.flush();
			//keyWriter.flush();
		}
		ipWriter.close();
		titleWriter.close();
		//descWriter.close();
		emailWriter.close();
		phoneWriter.close();
		//keyWriter.close();
	}

	/*private static boolean checkIfDomainURLMatch(String domain, String cURL)
	{
		String strippedURL = cURL.replaceFirst("http://", "").replaceFirst("www[.]", "");
		
		int count = StringUtils.countMatches(strippedURL, "/");
		URI uri = null;
		try
		{
			uri = new URI(strippedURL);
			String domain1 = uri.getHost();
			boolean isTopDomain = InternetDomainName.from(domain1).isTopPrivateDomain();
			if(isTopDomain && count == 0)
			{
				System.out.println("cURL is root "+cURL);
				return true;
			}
		} 
		catch (URISyntaxException e)
		{
			e.printStackTrace();
			return false;
		}
		
		
		
		if(strippedURL.contains("index.html?") || strippedURL.contains("index.htm?") || strippedURL.contains("index.php?") || strippedURL.contains("index.jsp?")
				|| strippedURL.contains("index.asp?") || strippedURL.contains("index/"))
		{
			return true;
		}
		return false;
	}*/

	/*private static void writeIntoKeyJSON(String domain, JsonObject cbJsonObject, CSVWriter keyWriter )
	{
		JsonElement keyJSONObj = cbJsonObject.get("KEY");

		// Key
		String keyArr1[] = new String[2];
		keyArr1[0] = domain; 
		if (keyJSONObj != null && !keyJSONObj.isJsonNull() && !keyJSONObj.getAsString().isEmpty())
		{
			keyArr1[1] = keyJSONObj.getAsString().replaceAll("\r"," ").replaceAll("\n", " ").replaceAll("\t", " ")
					.replaceAll("( )+", " ").trim();
			keyWriter.writeNext(keyArr1);
		}
	}*/

	private static void writeIntoPhoneJSON(String domain, JsonObject cbJsonObject, CSVWriter phoneWriter)
	{

		JsonArray phoneJSONArray = cbJsonObject.getAsJsonArray("PH");

		// Phone
		String phoneArr1[] = new String[2];
		phoneArr1[0] = domain; 
		if (phoneJSONArray != null)
		{
			int phoneArrSize = phoneJSONArray.size();
			//List<String> phoneArrayList = new ArrayList<String>();
			for (int i = 0; i < phoneArrSize; i++)
			{
				JsonElement phoneElem = phoneJSONArray.get(i);
				if (phoneElem != null && !phoneElem.isJsonNull() && !phoneElem.getAsString().isEmpty()
						&& !(phoneElem.getAsString().length() > 200))
				{
					//phoneArrayList.add(phoneElem.getAsString());
					phoneArr1[1] = phoneElem.getAsString();
					phoneWriter.writeNext(phoneArr1);
				}
			}/*
			if (!phoneArrayList.isEmpty())
			{
				phoneArr1[1] = String.join(";", (String[]) phoneArrayList.toArray(new String[0]));
				phoneWriter.writeNext(phoneArr1);
			}*/
		}
	}

	private static String getMD5HashForString(String selectSQL)
	{
		mDigest.reset();
	    mDigest.update(selectSQL.getBytes(Charset.forName("UTF8")));
	    final byte[] resultByte = mDigest.digest();
	    final String result = encodeHex(resultByte);
	    return result;
	}
	
	/* Inspired by encodeHex method from org.apache.commons.codec.binary.Hex class in the commons-codec package
	*  to avoid bundling the jar
	*  This method just converts the hex byte array to hex String value
	*/
	
	private static String encodeHex(byte[] data)
	{
		char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		int l = data.length;
	    char[] out = new char[l << 1];
	    int i = 0; for (int j = 0; i < l; ++i) 
	    {
		    out[(j++)] = DIGITS[((0xF0 & data[i]) >>> 4)];
		    out[(j++)] = DIGITS[(0xF & data[i])];
	    }
	    return new String(out);
	}
	private static MessageDigest mDigest = null;
	
	static
	{
		try
		{
			mDigest = MessageDigest.getInstance("MD5");
		}
		catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace(); 
		}
	}
	private static void writeIntoEmailJSON(String domain, JsonObject cbJsonObject, CSVWriter emailWriter)
	{
		JsonArray emailJSONArray = cbJsonObject.getAsJsonArray("EML");
		// Email
		String emailArr1[] = new String[5];
		emailArr1[0] = domain;
		emailArr1[1] = cbJsonObject.get("crURL").getAsString();
		emailArr1[2] = getMD5HashForString(emailArr1[1]);
		
		if (emailJSONArray != null)
		{
			int emailArrSize = emailJSONArray.size();
			for (int i = 0; i < emailArrSize; i++)
			{
				JsonElement emailElem = emailJSONArray.get(i);
				if (emailElem != null && !emailElem.isJsonNull() && !emailElem.getAsString().isEmpty()
						&& !(emailElem.getAsString().length() > 200))
				{
					emailArr1[3] = emailElem.getAsString();
					String emailAr[] = emailElem.getAsString().split("@");
					if(emailAr.length > 2)
					{
						//System.out.println("Invalid email "+emailElem.getAsString());
						return;
					}
					emailArr1[4] = emailAr[emailAr.length - 1];
					emailWriter.writeNext(emailArr1);
				}
			}
		}
	}

/*	private static void writeIntoDescJSON(String domain, JsonObject cbJsonObject, CSVWriter descWriter)
	{
		JsonArray descJSONArray = cbJsonObject.getAsJsonArray("Des");
		// Desc
		String descArr1[] = new String[2];
		descArr1[0] = domain;
		if (descJSONArray != null)
		{
			int descArrSize = descJSONArray.size();
			List<String> descArrayList = new ArrayList<String>();
			for (int i = 0; i < descArrSize; i++)
			{
				JsonElement descElem = descJSONArray.get(i);
				if (descElem != null && !descElem.isJsonNull() && !descElem.getAsString().isEmpty()
						&& !descArrayList.contains(descElem.getAsString()) && !(descElem.getAsString().length() > 200))
				{
					descArrayList.add(descElem.getAsString().replaceAll("\r"," ").replaceAll("\n", " ").replaceAll("\t", " ")
							.replaceAll("( )+", " ").trim());
				}
			}
			if (!descArrayList.isEmpty())
			{
				descArr1[1] = String.join(";", (String[]) descArrayList.toArray(new String[0]));
				descWriter.writeNext(descArr1);
			}
		}
	}
*/
	private static void writeIntoTitleJSON(String domain, JsonObject cbJsonObject, CSVWriter titleWriter )
	{
		// title
		JsonArray ttlJSONArray = cbJsonObject.getAsJsonArray("Ttl");
		String titleArr1[] = new String[4];
		titleArr1[0] = domain; 
		
		if (ttlJSONArray != null)
		{
			int titleArrSize = ttlJSONArray.size();
			List<String> ttlArrayList = new ArrayList<String>();
			for (int i = 0; i < titleArrSize; i++)
			{
				JsonElement titleElem = ttlJSONArray.get(i);
				if (titleElem != null && !titleElem.isJsonNull() && !titleElem.getAsString().isEmpty()
						&& !ttlArrayList.contains(titleElem.getAsString()) && !(titleElem.getAsString().length() > 200))
				{
					ttlArrayList.add(titleElem.getAsString().replaceAll("\r"," ").replaceAll("\n", " ").replaceAll("\t", " ")
							.replaceAll("( )+", " ").trim());
				}
			}
			if (!ttlArrayList.isEmpty())
			{
				titleArr1[1] = String.join(";", (String[]) ttlArrayList.toArray(new String[0]));
				// titleWriter.writeNext();
			}
			else
			{
				titleArr1[1] = "";
			}
			//titleWriter.writeNext(titleArr1);
		}
		
		// Desc
		JsonArray descJSONArray = cbJsonObject.getAsJsonArray("Des");
		//String descArr1[] = new String[1];
		if (descJSONArray != null)
		{
			int descArrSize = descJSONArray.size();
			List<String> descArrayList = new ArrayList<String>();
			for (int i = 0; i < descArrSize; i++)
			{
				JsonElement descElem = descJSONArray.get(i);
				if (descElem != null && !descElem.isJsonNull() && !descElem.getAsString().isEmpty()
						&& !descArrayList.contains(descElem.getAsString()) && !(descElem.getAsString().length() > 200))
				{
					descArrayList.add(descElem.getAsString().replaceAll("\r"," ").replaceAll("\n", " ").replaceAll("\t", " ")
							.replaceAll("( )+", " ").trim());
				}
			}
			if (!descArrayList.isEmpty())
			{
				titleArr1[2] = String.join(";", (String[]) descArrayList.toArray(new String[0]));
			}
			else
			{
				titleArr1[2] = "";
			}
			//titleWriter.writeNext(descArr1);
		}
		
		// Key
		JsonElement keyJSONObj = cbJsonObject.get("KEY");
		//String keyArr1[] = new String[1]; 
		if (keyJSONObj != null && !keyJSONObj.isJsonNull() && !keyJSONObj.getAsString().isEmpty())
		{
			titleArr1[3] = keyJSONObj.getAsString().replaceAll("\r"," ").replaceAll("\n", " ").replaceAll("\t", " ")
					.replaceAll("( )+", " ").trim();
		}
		else
		{
			titleArr1[3] = "";
		}
		titleWriter.writeNext(titleArr1);
	}

	private static void writeIntoSocialMediaJSON(String domain, JsonObject cbJsonObject, CSVWriter smWriter )
	{
		JsonElement smJSON = cbJsonObject.get("SM");
		if (smJSON != null)
		{
			List<String> smArrayList = new ArrayList<String>();
			smArrayList.add(domain); 
			smArrayList.add(cbJsonObject.get("crURL").getAsString());
			
			// facebook
			JsonArray facebookJsonArr = smJSON.getAsJsonObject().getAsJsonArray("FACEBOOK");
			if (facebookJsonArr != null)
			{
				int facebookArrSize = facebookJsonArr.size();
				String facebookArr1[] = new String[facebookArrSize];
				for (int i = 0; i < facebookArrSize; i++)
				{
					String facebookJsonString = facebookJsonArr.get(i).getAsString();
					if ((!facebookJsonString.contains("/dialog/feed?"))
							|| (!facebookJsonString.contains("/dialog/oauth"))
							|| (!facebookJsonString.contains("/share.php")))
					{
						facebookArr1[i] = facebookJsonString;
					}
				}
				String facebookLinks = String.join(",", facebookArr1);
				smArrayList.add(facebookLinks);
			} else
			{
				smArrayList.add("");
			}

			// youtube
			JsonArray youtubeJsonArr = smJSON.getAsJsonObject().getAsJsonArray("YOUTUBE");
			if (youtubeJsonArr != null)
			{
				int youtubeArrSize = youtubeJsonArr.size();
				String youtubeArr1[] = new String[youtubeArrSize];
				for (int i = 0; i < youtubeArrSize; i++)
				{
					String youtubeJsonString = youtubeJsonArr.get(i).getAsString();
					youtubeArr1[i] = youtubeJsonString;
				}
				String youtubeLinks = String.join(",", youtubeArr1);
				smArrayList.add(youtubeLinks);
			} else
			{
				smArrayList.add("");
			}

			// twitter
			JsonArray twitterJsonArr = smJSON.getAsJsonObject().getAsJsonArray("TWITTER");
			if (twitterJsonArr != null)
			{
				int twitterArrSize = twitterJsonArr.size();
				String twitterArr1[] = new String[twitterArrSize];
				for (int i = 0; i < twitterArrSize; i++)
				{
					String twitterJsonString = twitterJsonArr.get(i).getAsString();
					twitterArr1[i] = twitterJsonString;
				}
				String twitterLinks = String.join(",", twitterArr1);
				smArrayList.add(twitterLinks);
			}
			else
			{
				smArrayList.add("");
			}

			// googleplus
			JsonArray googleplusJsonArr = smJSON.getAsJsonObject().getAsJsonArray("GOOGLE");
			if (googleplusJsonArr != null)
			{
				int googleplusArrSize = googleplusJsonArr.size();
				String googleplusArr1[] = new String[googleplusArrSize];
				for (int i = 0; i < googleplusArrSize; i++)
				{
					String googleplusJsonString = googleplusJsonArr.get(i).getAsString();
					googleplusArr1[i] = googleplusJsonString;
				}
				String googleplusLinks = String.join(",", googleplusArr1);
				smArrayList.add(googleplusLinks);
			} else
			{
				smArrayList.add("");
			}

			// linkedin
			JsonArray linkedinJsonArr = smJSON.getAsJsonObject().getAsJsonArray("LINKEDIN");
			if (linkedinJsonArr != null)
			{
				int linkedinArrSize = linkedinJsonArr.size();
				String linkedinArr1[] = new String[linkedinArrSize];
				for (int i = 0; i < linkedinArrSize; i++)
				{
					String linkedinJsonString = linkedinJsonArr.get(i).getAsString();
					linkedinArr1[i] = linkedinJsonString;
				}
				String linkedinLinks = String.join(",", linkedinArr1);
				smArrayList.add(linkedinLinks);
			} else
			{
				smArrayList.add("");
			}

			// apple
			JsonArray appleJsonArr = smJSON.getAsJsonObject().getAsJsonArray("APPLE");
			if (appleJsonArr != null)
			{
				int appleArrSize = appleJsonArr.size();
				String appleArr1[] = new String[appleArrSize];
				for (int i = 0; i < appleArrSize; i++)
				{
					String appleJsonString = appleJsonArr.get(i).getAsString();
					appleArr1[i] = appleJsonString;
				}
				String appleLinks = String.join(",", appleArr1);
				smArrayList.add(appleLinks);
			} else
			{
				smArrayList.add("");
			}

			// windowsphone
			JsonArray windowsphoneJsonArr = smJSON.getAsJsonObject().getAsJsonArray("WINDOWSPHONE");
			if (windowsphoneJsonArr != null)
			{
				int windowsphoneArrSize = windowsphoneJsonArr.size();
				String windowsphoneArr1[] = new String[windowsphoneArrSize];
				for (int i = 0; i < windowsphoneArrSize; i++)
				{
					String windowsphoneJsonString = windowsphoneJsonArr.get(i).getAsString();
					windowsphoneArr1[i] = windowsphoneJsonString;
				}
				String windowsphoneLinks = String.join(";", windowsphoneArr1);
				smArrayList.add(windowsphoneLinks);
			} 
			else
			{
				smArrayList.add("");
			}

			smWriter.writeNext((String[]) smArrayList.toArray(new String[0]));

		}

	}

}
