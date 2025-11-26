
/**
 * Ponto de entrada principal da aplicação GymSolver
 * Esta classe existe apenas para facilitar a execução no VS Code
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando GymSolver...");
        try {
            // Chama o método main da aplicação JavaFX principal
            view.GymSolverApp.main(args);
        } catch (Exception e) {
            System.err.println("Erro ao iniciar a aplicação: " + e.getMessage());
            e.printStackTrace();
        }
    }
}