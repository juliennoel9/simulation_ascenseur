import java.util.ArrayList;
import java.util.Arrays;

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

        Cabine oldCabine = cabine;
        int oldNbPassagerCabine = cabine.nbPassagers();
        char oldIntention = cabine.intention();
        Passager[] oldTabPassagersCabine = cabine.getTableauPassager().clone();

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
                    //System.err.println("1");
                    if (newPass.sens() == oldIntention || !cabine.aDesPassagers()){
                        if (cabine.faireMonterPassager(newPass)){
                            cabine.changerIntention(newPass.sens());
                            echeancier.supprimePAP(newPass.getEvenementPietonArrivePalier());
                            nbMontent ++;
                            étage.getPassagers().remove(newPass);
                        }
                    }
                }else if (newPass.sens() == oldIntention) {
                    //System.err.println("2");
                    if (cabine.faireMonterPassager(newPass)){
                        cabine.changerIntention(newPass.sens());
                        echeancier.supprimePAP(newPass.getEvenementPietonArrivePalier());
                        nbMontent ++;
                        étage.getPassagers().remove(newPass);
                    }
                }else if(newPass.sens() != oldIntention && !cabine.aDesPassagers() && !immeuble.passagerAuDessus(cabine.étage) && !immeuble.passagerEnDessous(cabine.étage)){
                    //System.err.println("3");
                    if(cabine.faireMonterPassager(newPass)){
                        cabine.changerIntention(newPass.sens());
                        echeancier.supprimePAP(newPass.getEvenementPietonArrivePalier());
                        nbMontent ++;
                        étage.getPassagers().remove(newPass);
                    }
                } else if (étage.getPassagers().size() > 1 && étage.aPassagersMemeSansCabine(oldIntention)) {
                    //System.err.println("4");
                    newPass = cabine.getFirstWithSameIntention(oldIntention);
                    if (newPass != null){
                        cabine.changerIntention(newPass.sens());
                        cabine.faireMonterPassager(newPass);
                        echeancier.supprimePAP(newPass.getEvenementPietonArrivePalier());
                        nbMontent ++;
                        étage.getPassagers().remove(newPass);
                    }
                } else{
                    //System.err.println("5");
                    cabine.changerIntention(oldIntention);
                }
            }
            if (!modeParfait){
                boolean tmpPassagers = cabine.aDesPassagers();
                if (cabine.faireMonterPassager(newPass)){
                    if (tmpPassagers){
                        cabine.changerIntention(oldIntention);
                    }
                    if (oldNbPassagerCabine==0){
                        cabine.changerIntention(newPass.sens());
                    }
                    echeancier.supprimePAP(newPass.getEvenementPietonArrivePalier());
                    nbMontent ++;
                    étage.getPassagers().remove(newPass);
                }
            }
            if(!modeParfait){
                int i = 0;
                ArrayList<Passager> clone = new ArrayList<>(étage.getPassagers());
                while(étage.getPassagers().size()!=0 && i < étage.getPassagers().size()){
                    if (cabine.faireMonterPassager(étage.getPassagers().get(i))){
                        nbMontent ++;
                        echeancier.supprimePAP(étage.getPassagers().get(i).getEvenementPietonArrivePalier());
                        clone.remove(étage.getPassagers().get(i));
                    }
                    i++;
                }
                étage.setPassagers(clone);
                if(oldNbPassagerCabine==0 || nbDescendent == 4){
                    cabine.recalculerIntentionInfernale();
                }
            }else {
                if (nbMontent>=1){
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
        }
        if (cabine.intention()!='-'){
            EvenementFermeturePorteCabine evenementFermeturePorteCabine = new EvenementFermeturePorteCabine(this.date+nbDescendent*tempsPourEntrerOuSortirDeLaCabine+nbMontent*tempsPourEntrerOuSortirDeLaCabine+this.tempsPourOuvrirOuFermerLesPortes);
            echeancier.ajouter(evenementFermeturePorteCabine);
        }

        if (nbMontent == 0 && cabine.aDesPassagers()){
            cabine.changerIntention(oldIntention);
        }
        if (!modeParfait && Arrays.equals(cabine.getTableauPassager(), oldTabPassagersCabine) && oldCabine.aDesPassagers()){
            cabine.changerIntention(oldIntention);
        }

        if (!modeParfait && cabine.aucunPassagerMemeSens()){
            cabine.recalculerIntentionInfernale();
        }

        if (!modeParfait && ( (oldIntention=='v' && immeuble.passagerEnDessous(étage)) || (oldIntention=='^' && immeuble.passagerAuDessus(étage)) )) {
            cabine.changerIntention(oldIntention);
        }

        if (!modeParfait && cabine.aPassagersMontantEtDescendant()){
            cabine.changerIntention(oldIntention);
        }

        if(étage==immeuble.étageLePlusHaut()){
            cabine.changerIntention('v');
        }
        if(étage==immeuble.étageLePlusBas() && (immeuble.passagerEnDessous(étage) || immeuble.passagerAuDessus(étage) || cabine.aDesPassagers())){
            cabine.changerIntention('^');
        }


        if (cabine.intention() != '-' && !echeancier.haveFPC()){
            EvenementFermeturePorteCabine evenementFermeturePorteCabine = new EvenementFermeturePorteCabine(this.date+nbDescendent*tempsPourEntrerOuSortirDeLaCabine+nbMontent*tempsPourEntrerOuSortirDeLaCabine+this.tempsPourOuvrirOuFermerLesPortes);
            echeancier.ajouter(evenementFermeturePorteCabine);
        }
    }

}
