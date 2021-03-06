import org.jsoup.select.Elements;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.io.IOException;
import java.util.*;

public class Index {

	// Index: map of words to URL and their counts
	private Jedis jedis;
	private StopWords stopWords = new StopWords();

	public Index(Jedis jedis) throws IOException {
	    this.jedis = jedis;
	}

	public double computeTFIDF (String term, String url) {
        int pageCount = getCount(term); // number of pages
        int totalPageCount = (jedis.smembers("numPages")).size();
        double TF = getDocFreq(term, url);
        double IDF_fract = pageCount/totalPageCount;
        double IDF = Math.log(IDF_fract);
        System.out.println(TF*IDF);
        return TF*IDF;
	}

    public double getDocFreq(String term, String url) {
        HashMap tc = (HashMap) jedis.hgetAll("TermCounter: " + url);
        Object temp = tc.get(term);
        double termCount = Integer.valueOf(String.valueOf(temp));
        return (termCount / (double) tc.size());
    }

    public int getCount(String term) { //number of pages
        Set<String> countSet = jedis.smembers("countSet: " + term);
        int total = 0;
        for (String pageCount: countSet) {
            total += Integer.valueOf(pageCount);
        }
        return total;
    }


//	public void add(String term, TermCounter tc) {
//        Set<TermCounter> set = get(term);
//        if (set == null){
//            set = new HashSet<>();
//            index.put(term,set);
//        }
//
//        set.add(tc);
//
//		// if we're seeing a term for the first time, make a new Set
//		// otherwise we can add the term to an existing Set
//	}

	public Map<String, Integer> get(String term) {
        Set<String> urlSet = jedis.smembers("urlSet: " + term);
        Map<String, Integer> countMap = new HashMap();
        for (String url: urlSet) {
            String count = jedis.hget("TermCounter: " + url, term);
            countMap.put(url, Integer.valueOf(count));
        }
	    return countMap;
	}

	public void indexPage(String url, Elements paragraphs) throws IOException {
		// make a TermCounter and count the terms in the paragraphs
        TermCounter tc = new TermCounter(url);
        tc.processElements(paragraphs);

        Transaction t = jedis.multi();

        String hashname = "TermCounter: " + url;
        t.del(hashname);        //delete if already indexed

        Set<String> badWords = stopWords.getStopWords();

        // for each term in the TermCounter, add the TermCounter to the index
		for(String term : tc.keySet()){
		    if (!badWords.contains(term)) {
                t.hset(hashname, term, tc.get(term).toString());
                String urlSetKey = "urlSet: " + term;
                t.sadd(urlSetKey, url);
                String totalCountKey = "countSet: " + term;
//                System.out.print(tc.get(term).toString());
                t.sadd(totalCountKey, "1");
                t.sadd("numPages", "1");
            }
        }
        List<Object> res = t.exec();
//        System.out.println(res.toString());
	}

//	public void printIndex() {
//		// loop through the search terms
//		for (String term: keySet()) {
////			System.out.println(term);
//
//			// for each term, print the pages where it appears
//			Set<TermCounter> tcs = get(term);
//			for (TermCounter tc: tcs) {
//				Integer count = tc.get(term);
////				System.out.println("    " + tc.getLabel() + " " + count);
//			}
//		}
//	}

//	public Set<String> keySet() {
//		return index.keySet();
//	}

	public static void main(String[] args) throws IOException {
		WikiFetcher wf = new WikiFetcher();
		Jedis jedis = JedisMaker.make();
		Index indexer = new Index(jedis);

		String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
		Elements paragraphs = wf.fetchWikipedia(url);
		indexer.indexPage(url, paragraphs);

		url = "https://en.wikipedia.org/wiki/Programming_language";
		paragraphs = wf.fetchWikipedia(url);
		indexer.indexPage(url, paragraphs);

		indexer.computeTFIDF("language", url);

//		indexer.printIndex();

	}
}
