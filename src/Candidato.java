import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Candidato implements Comparable<Candidato> {
	public int id;
	public int peso; 
	public int localAtual;
	public int grauSatisfacao;
	public List<Integer> preferencias; //Ordenado pela preferência 
	public Candidato(int id, int peso, int localAtual, Integer... preferencias)
	{
		this.id = id;
		this.peso = peso;
		this.localAtual = localAtual;
		this.preferencias = new ArrayList<Integer>(Arrays.asList(preferencias));
		this.grauSatisfacao = this.preferencias.size();
	}
	@Override
	public int compareTo(Candidato o) {
		// TODO Auto-generated method stub
		return (this.peso-o.peso);
	}
}
