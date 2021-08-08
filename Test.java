import choco.Choco;
import choco.cp.model.CPModel;
import choco.cp.solver.CPSolver;
import choco.kernel.model.Model;
import choco.kernel.model.variables.integer.IntegerVariable;
import choco.kernel.solver.Solver;

import static choco.Choco.eq;

public class Test {
    public static void main(String[] args) {
        //la taille du probléme
        int nbvar = 9;
        //soduko initiale
        int[][] soduko= new int[][]{
                {5, 0, 3, 0, 0, 4, 2, 0, 0},
                {2, 7, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 2, 0, 0, 0, 0},
                {0, 2, 0, 4, 0, 0, 0, 0, 7},
                {4, 3, 0, 0, 8, 0, 0, 0, 2},
                {0, 5, 0, 0, 0, 0, 8, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 5, 9, 0, 0, 0, 4},
                {3, 0, 5, 0, 1, 0, 9, 0, 6}
        };
        //declaration de la model
        Model m = new CPModel();
        //Declaration des variables et les demaines.
        IntegerVariable[][] var = new IntegerVariable[nbvar][nbvar];
        for (int i = 0; i < nbvar; i++) {
            for (int j = 0; j < nbvar; j++) {
                var[i][j] = Choco.makeIntVar("C"+(i+1)+""+ ( j+1), 1,9);
                if(soduko[i][j] != 0){
                    //donnes initiales
                    m.addConstraint(eq(var[i][j], soduko[i][j]));
                }
                m.addVariable(var[i][j]);
            }

        }
        //Declartion des contraintes
        //C1:contrainte ligne.
        for (int i = 0; i < nbvar; i++) {
            for (int j = 0; j < nbvar; j++) {
                for (int k = j + 1; k < nbvar; k++) {
                    m.addConstraint(Choco.neq(var[i][j], var[i][k]));
                }
            }
        }
        //C2:contrainte colonne
        for (int j = 0; j < nbvar; j++) {
            for (int i = 0; i < nbvar; i++) {
                for (int k = i + 1; k < nbvar; k++) {
                    m.addConstraint(Choco.neq(var[i][j], var[k][j]));
                }
            }
        }
        //C3:contrainte carré/
      /*for(int a=0;a<3;a++) {
            for (int b = 0; b < 3; b++) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j <3 ; j++) {
                        if (a != i && b != j){
                            m.addConstraint(Choco.neq(var[i][j], var[a][b]));
                        }
                    }
                }
            }
        }*/

        for(int X=0; X<3; X+=3){
            for(int Y=0; Y<3; Y+=3){
                for(int a=X;a<X+3;a++) {
                    for (int b = Y; b < Y+3; b++) {
                        for (int i = X; i < X+3; i++) {
                            for (int j = Y; j <Y+3 ; j++) {
                                if (a != i && b != j){
                                    m.addConstraint(Choco.neq(var[i][j], var[a][b]));
                                }
                            }
                        }
                    }
                }
            }
        }
        //Declaration du solveur
        Solver s =new CPSolver();
//lecture du modele par le solveur
        s.read(m);
//recherche de la premier solution
        s.solve();
//Affichage des resultats
        //Affichage simple
        int c=0;
        do {
            c++;
            System.out.println("solution number:"+c);
            for (int i = 0; i < nbvar; i++) {
                for (int j = 0; j < nbvar; j++) {
                    System.out.print(s.getVar(var[i][j]) + "\t");
                }
                System.out.println();
            }
        }while (s.nextSolution());

    }
}
