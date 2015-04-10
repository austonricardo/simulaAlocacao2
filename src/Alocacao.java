import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Alocacao {
	public static List<Candidato> candidatos;
	public static List<Local> ofertas; //Todos os locais até com 0 vagas
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		popular();
		imprimirSituacao(candidatos);
		System.out.println("----------------Result 1----------------------------");
		List<Candidato> result =alocar();
		imprimirSituacao(result);
		System.out.println("----------------Result 2----------------------------");
		List<Candidato> result2 =alocar2();
		imprimirSituacao(result2);
	}
	
	public static void popular(){
		 candidatos = new ArrayList<Candidato>();
		 candidatos.add(new Candidato(1, 1, 3, 1,2));
		 candidatos.add(new Candidato(2, 2, 2, 3,1));
		 candidatos.add(new Candidato(3, 3, 1, 2,3));
		 candidatos.add(new Candidato(4, 4, 1, 3));
		 candidatos.add(new Candidato(5, 5, 4, 1,2));
		 candidatos.add(new Candidato(6, 6, 4, 1,2,3));
		 candidatos.add(new Candidato(7, 7, 1, 2,3));
		 candidatos.add(new Candidato(8, 8, 1, 3));
		 candidatos.add(new Candidato(9, 9, 4, 1,2));
		 candidatos.add(new Candidato(10, 10, 4, 1,2,3));
		 candidatos.add(new Candidato(11, 11, 3, 1,2));
		 candidatos.add(new Candidato(12, 12, 2, 3,1));
		 candidatos.add(new Candidato(13, 13, 1, 2,3));
		 candidatos.add(new Candidato(14, 14, 1, 3));
		 candidatos.add(new Candidato(15, 15, 4, 1,2));
		 candidatos.add(new Candidato(16, 16, 4, 1,2,3));
		 candidatos.add(new Candidato(17, 17, 1, 2,3));
		 candidatos.add(new Candidato(18, 18, 1, 3));
		 candidatos.add(new Candidato(19, 19, 4, 1,2));
		 candidatos.add(new Candidato(20, 20, 4, 1,2,3));
		 
		 ofertas = new ArrayList<Local>();
		 ofertas.add(new Local(1, 0));
		 ofertas.add(new Local(2, 0));
		 ofertas.add(new Local(3, 0));
		 ofertas.add(new Local(4, 0));
	} 

	//Super concorrência pra um mesmo lugar
	public static void popular2() {
		 candidatos = new ArrayList<Candidato>();
		 candidatos.add(new Candidato(1, 1, 3, 1,2));
		 candidatos.add(new Candidato(2, 2, 2, 1,3));
		 candidatos.add(new Candidato(3, 3, 1, 2));
		 candidatos.add(new Candidato(4, 4, 3, 1,2));
		 candidatos.add(new Candidato(4, 4, 4, 1,2));
		 candidatos.add(new Candidato(5, 5, 4, 1,2,3));
		 
		 ofertas = new ArrayList<Local>();
		 ofertas.add(new Local(1, 0));
		 ofertas.add(new Local(2, 0));
		 ofertas.add(new Local(3, 0));
		 //ofertas.add(new Local(4, 0));
	} 	
	
	public static List<Candidato> alocar()
	{
		List<Candidato> result = candidatos;
		Collections.sort(result);
		
		int movimentacoes;
		do{
			movimentacoes=0;
			for(Candidato c:result){
				for(int i =0; i< c.grauSatisfacao;i++){
					Integer pref = c.preferencias.get(i);
					Local local = recuperaLocal(pref);
					if(local.vagasAbertas>0)
					{
						movimentaLocal(c, local);
						movimentacoes++;
						break;
					}
				}
			}
			
		}while(movimentacoes>0);
		
		return result;
	}

	public static List<Candidato> alocar2()
	{
		List<Candidato> result = candidatos;
		Collections.sort(result);
		Local limbo = new Local(99, 0);
		ofertas.add(limbo);
		for(Candidato c2:result)
		{
			c2.preferencias.add(c2.localAtual);
			movimentaLocal(c2, limbo);
		}
		for(int i=0; i< result.size(); i++){
			Candidato candidato = result.get(i);
			boolean monvimento = false;
			for(Integer pref:candidato.preferencias){
				Local local = recuperaLocal(pref);
				if(local.vagasAbertas>0)
				{
					movimentaLocal(candidato, local);
					monvimento = true;
					break;
				}
			}
			
			//checa se não houve movimentação, e piora o cara anterior
			if(!monvimento){
				System.out.println("BT: C" + candidato.id + " sem alocação disponível");
				//ver quem da cadeia de cima pode ser piorado
				int candidatoApiorarIndice=0;
				for(int k = i-1; k >= 0;  k--)
				{
					Candidato candidatoApiorar = result.get(k);
					if (candidatoApiorar.grauSatisfacao < candidatoApiorar.preferencias.size()-1)//Ainda dá pra piorar
					{
						candidatoApiorarIndice=k;
						break;
					}
				}
				Candidato candidatoAnterior = result.get(candidatoApiorarIndice);
				//Piora a situacao do cara anterior
				Local proximaPreferencia = recuperaLocal(candidatoAnterior.preferencias.get(candidatoAnterior.grauSatisfacao+1));
				movimentaLocal(candidatoAnterior, proximaPreferencia);
				//Move os cara abaixo dele para o limbo novamente
				for(int j=candidatoApiorarIndice+1; j< result.size(); j++){
					movimentaLocal(result.get(j), limbo);
				}
				monvimento=true;
				i = candidatoApiorarIndice;
			}
		}
		
		return result;
	}	
	
	public static void movimentaLocal(Candidato c, Local para)
	{
		System.out.println("MOV:C" + c.id + " de L"+c.localAtual + " para L"+para.id);
		Local localAtual = recuperaLocal(c.localAtual);
		localAtual.vagasAbertas++;
		c.localAtual=para.id;
		c.grauSatisfacao = c.preferencias.indexOf(para.id);
		para.vagasAbertas--;
	}
	
	public static Local recuperaLocal(int id)
	{
		for(Local c: ofertas)
		{
			if(c.id==id) return c;
		}
		return null;
	}
	
	public static void imprimirSituacao(List<Candidato> list)
	{
		for(Candidato c:list)
		{
			System.out.println("C" + c.id + " L"+c.localAtual + " S" + c.grauSatisfacao);
		}
	}

}
