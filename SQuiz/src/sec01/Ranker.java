package sec01;

public class Ranker implements Comparable<Ranker>{
	String name;
	String score;
	
	public Ranker(String name, String score) {
		this.name = name;
		this.score = score;
	}
	public Ranker() {
		name = "";
		score = "";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getScore() {
		return score;
	}
	public int getIntScore() {
		return Integer.parseInt(score);
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String toString() {
		return String.format("%s : %s", name, score);
	}
	
	@Override
	public int compareTo(Ranker r) {
		if(Integer.parseInt(this.score) < Integer.parseInt(r.score))
			return 1;
		else if(Integer.parseInt(this.score) == Integer.parseInt(r.score))
			return 0;
		else
			return -1;
	}
	
}
