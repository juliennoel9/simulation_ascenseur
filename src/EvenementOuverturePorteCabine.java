import java.util.ArrayList;

public class EvenementOuverturePorteCabine extends Evenement {
    /* OPC: Ouverture Porte Cabine
       L'instant précis ou la porte termine de s'ouvrir.
    */

    public EvenementOuverturePorteCabine(long d) {
	super(d);
    }

    public void afficheDetails(StringBuilder buffer, Immeuble immeuble) {
	buffer.append("OPC");
    }

    public void traiter(Immeuble immeuble, Echeancier echeancier) {
        Cabine cabine = immeuble.cabine;
        Etage étage = cabine.étage;

        //notYetImplemented();
        cabine.porteOuverte = true;

        assert cabine.porteOuverte;

        char oldIntention = cabine.intention();

        if (immeuble.passagerAuDessus(étage)){
            cabine.changerIntention('^');
        }else if (immeuble.passagerEnDessous(étage)){
            cabine.changerIntention('v');
        }else {
            cabine.changerIntention('-');
        }

        long nbDescendent= cabine.faireDescendrePassagers(immeuble,this.date);

        long nbMontent = 0;

        if (étage.aDesPassagers()){
            Passager newPass = étage.getPassagers().get(0);
            if (modeParfait){
                newPass = cabine.choisirQuiMonte();
            }
            if (modeParfait && newPass != null){
                if ((oldIntention == '^' && !immeuble.passagerAuDessus(cabine.étage)) ||
                        (oldIntention == 'v' && !immeuble.passagerEnDessous(cabine.étage))){
                    if (cabine.faireMonterPassager(newPass)){
                        cabine.changerIntention(newPass.sens());
                        echeancier.supprimePAP(newPass.getEvenementPietonArrivePalier());
                        nbMontent ++;
                        étage.getPassagers().remove(newPass);
                    }
                }else if (newPass.sens() == oldIntention) {
                    if (cabine.faireMonterPassager(newPass)){
                        cabine.changerIntention(newPass.sens());
                        echeancier.supprimePAP(newPass.getEvenementPietonArrivePalier());
                        nbMontent ++;
                        étage.getPassagers().remove(newPass);
                    }
                }else if(newPass.sens() != oldIntention && !cabine.aDesPassagers() && !immeuble.passagerAuDessus(cabine.étage) && !immeuble.passagerEnDessous(cabine.étage)){
                    if(cabine.faireMonterPassager(newPass)){
                        cabine.changerIntention(newPass.sens());
                        echeancier.supprimePAP(newPass.getEvenementPietonArrivePalier());
                        nbMontent ++;
                        étage.getPassagers().remove(newPass);
                    }
                }else{
                    cabine.changerIntention(oldIntention);
                }
            }
            if (!modeParfait){
                if (cabine.faireMonterPassager(newPass)){
                    cabine.changerIntention(newPass.sens());
                    echeancier.supprimePAP(newPass.getEvenementPietonArrivePalier());
                    nbMontent ++;
                    étage.getPassagers().remove(newPass);
                }
            }
            if(!modeParfait){
                int i = 0;
                while(étage.getPassagers().size()!=0 && i < étage.getPassagers().size()){
                    if (cabine.faireMonterPassager(étage.getPassagers().get(i))){
                        nbMontent ++;
                        echeancier.supprimePAP(étage.getPassagers().get(i).getEvenementPietonArrivePalier());
                        étage.getPassagers().remove(i);
                    }
                    i++;
                }
                cabine.recalculerIntentionInfernale();
            }else {
                int j = 0;
                ArrayList<Passager> clone = new ArrayList<>(étage.getPassagers());
                while(j<étage.getPassagers().size()){
                    if (étage.getPassagers().get(j).sens() == cabine.intention()) {
                        if (cabine.faireMonterPassager(étage.getPassagers().get(j))) {
                            nbMontent++;
                            echeancier.supprimePAP(étage.getPassagers().get(j).getEvenementPietonArrivePalier());
                            clone.remove(étage.getPassagers().get(j));
                        }
                    }
                    j++;
                }
                étage.setPassagers(clone);
            }
        }
        if (cabine.intention()!='-'){
            EvenementFermeturePorteCabine evenementFermeturePorteCabine = new EvenementFermeturePorteCabine(this.date+nbDescendent*tempsPourEntrerOuSortirDeLaCabine+nbMontent*tempsPourEntrerOuSortirDeLaCabine+this.tempsPourOuvrirOuFermerLesPortes);
            echeancier.ajouter(evenementFermeturePorteCabine);
        }
        if(étage==immeuble.étageLePlusHaut()){
            cabine.changerIntention('v');
        }
        if(étage==immeuble.étageLePlusBas()){
            cabine.changerIntention('^');
        }

        if (modeParfait && nbMontent == 0 && cabine.aDesPassagers()){
            cabine.changerIntention(oldIntention);
        }
    }

}
