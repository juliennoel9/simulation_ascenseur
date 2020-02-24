public class EvenementPietonArrivePalier extends Evenement {
    private Passager passager;
    private Etage étage;
    /* PAP: Pieton Arrive Palier
       L'instant précis ou un passager qui à décidé de continuer à pieds arrive sur un palier donné.
       Classe à faire complètement par vos soins.
    */

    public void afficheDetails(StringBuilder buffer, Immeuble immeuble) {
        buffer.append("PAP " + étage.numéro() + " #" + passager.numéroDeCréation());
    }

    public void traiter(Immeuble immeuble, Echeancier echeancier) {
        //notYetImplemented();
    }

    public EvenementPietonArrivePalier(long d, Etage e, Passager passager) {
	// Signature approximative et temporaire... juste pour que cela compile.
	super(d);
	this.étage = e;
	this.passager = passager;
    }
    
}
