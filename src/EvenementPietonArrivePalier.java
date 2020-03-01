public class EvenementPietonArrivePalier extends Evenement {
    private Passager passager;
    private Etage étage;

    private int numEtageProchainPAP;
    /* PAP: Pieton Arrive Palier
       L'instant précis ou un passager qui à décidé de continuer à pieds arrive sur un palier donné.
       Classe à faire complètement par vos soins.
    */

    public void afficheDetails(StringBuilder buffer, Immeuble immeuble) {
        buffer.append("PAP " + this.numEtageProchainPAP + " #" + passager.numéroDeCréation());
    }

    public void traiter(Immeuble immeuble, Echeancier echeancier) {
        Etage newEtage = étage.pietonMonteEtage(this.passager, this.date, echeancier, immeuble);
        if(newEtage!=null){
            this.date = this.date + Global.tempsPourMonterOuDescendreUnEtageAPieds;
            this.étage = immeuble.étage(newEtage.numéro());
            echeancier.ajouter(this);
            this.numEtageProchainPAP = (passager.sens() == '^' ? 1 : -1) + newEtage.numéro();
        }
    }

    public EvenementPietonArrivePalier(long d, Etage e, Passager passager) {
	// Signature approximative et temporaire... juste pour que cela compile.
	super(d);
	this.étage = e;
	this.passager = passager;
	this.numEtageProchainPAP = e.numéro();
    }
    
}
