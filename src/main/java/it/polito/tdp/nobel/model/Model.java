package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.management.loading.PrivateClassLoader;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {
	private List<Esame> partenza ;
	private Set<Esame> soluzioneMigliore;
	private double mediaSoluzioneMigliore;
	
	public Model () {
		EsameDAO dao= new EsameDAO() ;
		this.partenza = dao.getTuttiEsami() ; 
	}
	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		Set <Esame> parziale = new HashSet<Esame>() ;
		soluzioneMigliore = new HashSet<Esame>() ;
		mediaSoluzioneMigliore = 0 ;
		
		//cerca(parziale, 0, numeroCrediti) ;
		cerca2(parziale, 0, numeroCrediti);
		
		return soluzioneMigliore;	
	}

	private void cerca2(Set<Esame> parziale, int L, int m) {
		// casi terminali
		
		int crediti = sommaCrediti(parziale) ;
		
		if(crediti > m) {
			return ;
		}
		
		if( crediti == m) {
			double media = calcolaMedia(parziale) ;
			if (media > mediaSoluzioneMigliore) {
				soluzioneMigliore = new HashSet<>(parziale) ;
				mediaSoluzioneMigliore = media ;
			}
			return ;
		}
		
		// sicuramnte crediti < m (in teoria posso ancora andare avanti)
		// 1) L = N -> non ci sono piú esami da aggiungere
		if( L == partenza.size()) {
			return ;
		}
		
		// genero i sottoproblemi( non rientro nei casi terminali)
		parziale.add(partenza.get(L)) ;
		cerca2(parziale, L+1, m) ; // escploriamo il ramo dove ho aggiunto l'élemento
		parziale.remove(partenza.get(L)) ; // BACKTRACKING
		cerca2(parziale, L+1, m);
		
	}
	
	
	// COMPLESSITA : N! 
	private void cerca(Set<Esame> parziale,int L,  int m) {
		// casi terminali
		
		int crediti = sommaCrediti(parziale) ;
		
		if(crediti > m) {
			return ;
		}
		
		if( crediti == m) {
			double media = calcolaMedia(parziale) ;
			if (media > mediaSoluzioneMigliore) {
				soluzioneMigliore = new HashSet<>(parziale) ;
				mediaSoluzioneMigliore = media ;
			}
			return ;
		}
		
		// sicuramnte crediti < m (in teoria posso ancora andare avanti)
		// 1) L = N -> non ci sono piú esami da aggiungere
		if( L == partenza.size()) {
			return ;
		}
		
		// genero i sottoproblemi( non rientro nei casi terminali)
		for( Esame e: partenza) {
			if(!parziale.contains(e)) {
				parziale.add(e) ;
				cerca(parziale, L+1, m);
				parziale.remove(e) ; // Backtracking
			}
		}	
	}

	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}
}
