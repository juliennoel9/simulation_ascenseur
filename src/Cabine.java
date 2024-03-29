public class Cabine extends Global {
    /* Dans cette classe, vous pouvez ajouter/enlever/modifier/corriger les méthodes, mais vous ne
       pouvez pas ajouter des attributs (variables d'instance).
    */
    
    public Etage étage; // actuel, là ou se trouve la Cabine, jamais null.

    public boolean porteOuverte;

    private char intention; // '-' ou 'v' ou '^'

    private Passager[] tableauPassager;
    /* Ceux qui sont actuellement dans la Cabine. On ne décale jamais les élements.
       Comme toute les collections, il ne faut pas l'exporter.
       Quand on cherche une place libre, on fait le parcours de la gauche vers la droite.
     */

    public Cabine(Etage e) {
	assert e != null;
	étage = e;
	tableauPassager = new Passager[nombreDePlacesDansLaCabine];
	porteOuverte = true;
	intention = '-';
    }

    public void afficheDans(StringBuilder buffer) {
	buffer.append("Contenu de la cabine: ");
	for (Passager p: tableauPassager) {
	    if (p == null) {
		buffer.append("null");		
	    } else {
		p.afficheDans(buffer);
	    }
	    buffer.append(' ');
	}
	assert (intention == '-') || (intention == 'v') || (intention == '^');
	buffer.append("\nIntention de la cabine: " + intention);
    }

    /* Pour savoir si le passager p est bien dans la Cabine.
       Attention, c'est assez lent et c'est plutôt une méthode destinée à être 
       utilisée les asserts.
    */
    public boolean transporte(Passager p) {
	assert p != null;
	for (int i = tableauPassager.length - 1 ; i >= 0  ; i --) {
	    if (tableauPassager[i] == p) {
		return true;
	    }
	}
	return false;
    }

    public char intention() {
		assert (intention == '-') || (intention == 'v') || (intention == '^');
		return intention;
    }

    public void changerIntention(char s){
		assert (s == '-') || (s == 'v') || (s == '^');
		intention = s;
    }

    public boolean faireMonterPassager(Passager p) { 
		assert p != null;
		assert ! transporte(p);
		if (modeParfait) {
			if (intention != p.sens()) {
				return false;
			}
		}
		for (int i=0 ; i<tableauPassager.length ; i++) {
			if(tableauPassager[i]==null){
				tableauPassager[i]=p;
				return true;
			}
		}
		return false;
    }

    public int faireDescendrePassagers(Immeuble immeuble,long d){
	int c=0;
	int i=tableauPassager.length-1;
	while(i>=0){
	    if(tableauPassager[i]!=null){
		assert transporte(tableauPassager[i]);
		if(tableauPassager[i].étageDestination() == étage){
		    immeuble.ajouterCumul(d-tableauPassager[i].dateDépart());
		    immeuble.nombreTotalDesPassagersSortis++;
		    tableauPassager[i]=null; 
		    c++;
		}
	    }
	    i--;
	}
	return c;
    }

    public boolean passagersVeulentDescendre(){
	int i=tableauPassager.length-1;
	while(i>=0){
	    if(tableauPassager[i]!=null){
		assert transporte(tableauPassager[i]);
		if(tableauPassager[i].étageDestination() == étage){
		    return true;
		}
	    }
	    i--;
	}
	return false;
    }

	public Passager[] getTableauPassager() {
		return tableauPassager;
	}

	public boolean aDesPassagers() {
		for (int i=0; i<this.tableauPassager.length; i++) {
			if (this.tableauPassager[i] != null){
				return true;
			}
		}
		return false;
	}

	public int nbPassagers() {
    	int res = 0;
		for (Passager p : this.tableauPassager) {
			if (p != null){
				res++;
			}
		}
		return res;
	}

	public char recalculerIntentionInfernale(){
    	char sens = '-';
		if (this.aDesPassagers()){
			int diffEtage = 10;
			Passager passagerPlusProche=this.getTableauPassager()[0];
			for (int i1 = 0; i1 < this.getTableauPassager().length; i1++) {
				if (this.getTableauPassager()[i1] != null ) {
					if (Math.abs(étage.numéro() - this.getTableauPassager()[i1].numéroDestination()) < diffEtage) {
						diffEtage = Math.abs(étage.numéro() - this.getTableauPassager()[i1].numéroDestination());
						passagerPlusProche = this.getTableauPassager()[i1];
					}
				}
			}
			this.changerIntention(passagerPlusProche.sens());
			sens = passagerPlusProche.sens();
		}
		return sens;
	}

	public Passager choisirQuiMonte(){
    	Passager passagerMontant = null;
    	Passager first = étage.getPassagers().get(0);
    	if (étage.aDesPassagers()){
			int diffEtage = 10;
			passagerMontant=étage.getPassagers().get(0);
			for (int i1 = 0; i1 < étage.getPassagers().size(); i1++) {
				if (étage.getPassagers().get(i1) != null ) {
					if (Math.abs(étage.numéro() - étage.getPassagers().get(i1).numéroDestination()) < diffEtage) {
						diffEtage = Math.abs(étage.numéro() - étage.getPassagers().get(i1).numéroDestination());
						if (étage.getPassagers().get(i1).sens() != first.sens()){
							passagerMontant = étage.getPassagers().get(i1);
						}
					}
				}
			}
			this.changerIntention(passagerMontant.sens());
		}
    	return passagerMontant;
	}

	public Passager getFirstWithSameIntention(char intention) {
		for (Passager p : this.étage.getPassagers()) {
			if (p.sens() == intention) {
				return p;
			}
		}
		return null;
	}

	public Passager choisirQuiMonte2() {
    	Passager montant = null;
    	Passager first = étage.getPassagers().get(0);
    	if (étage.aDesPassagers()){
    		int diffEtage = 10;
			for (int i = 0; i < étage.getPassagers().size(); i++) {
				Passager temp = étage.getPassagers().get(i);
				if (temp != null) {

				}
			}
		}
    	return montant;
	}

	public Passager choisirQuiMonte3() {
		Passager passagerMontant = null;
		Passager first = étage.getPassagers().get(0);
		if (étage.aDesPassagers()){
			int diffEtage = 10;
			passagerMontant=étage.getPassagers().get(0);
			for (int i1 = 0; i1 < étage.getPassagers().size(); i1++) {
				if (étage.getPassagers().get(i1) != null ) {
					if (Math.abs(étage.numéro() - étage.getPassagers().get(i1).numéroDestination()) < diffEtage) {
						diffEtage = Math.abs(étage.numéro() - étage.getPassagers().get(i1).numéroDestination());
						if (this.aDesPassagers()){
							if (étage.getPassagers().get(i1).sens()==this.intention){
								passagerMontant = étage.getPassagers().get(i1);
							}
						}else if (étage.getPassagers().get(i1).sens() != first.sens()){
							passagerMontant = étage.getPassagers().get(i1);
						}
					}
				}
			}
			this.changerIntention(passagerMontant.sens());
		}
		return passagerMontant;
	}

	public boolean aucunPassagerMemeSens() {
		for (Passager p : this.getTableauPassager()) {
			if (p != null) {
				if (p.sens() == this.intention) return false;
			}
		}
		return true;
	}

	public boolean aPassagersMontantEtDescendant () {
    	boolean monte = false;
    	boolean descend = false;
		for (Passager p : this.getTableauPassager()) {
			if (p != null) {
				if (p.sens() == 'v'){
					descend = true;
				} else if (p.sens() == '^') {
					monte = true;
				}
			}
		}
		return (monte && descend);
	}

	public char getSensPremierPassager() {
		for (Passager p : this.getTableauPassager()) {
			if (p != null) {
				return p.sens();
			}
		}
		return '-';
	}
}
