# CLAUDE.md — Gestion Daara (projet pédagogique Java)

Contexte pour les futures sessions Claude Code sur ce dépôt.

## But du projet

Application de bureau **Java Swing** de gestion d'une *daara* (école coranique) :
on gère des **maîtres**, des **classes** (halqas), des **talibés** (élèves) et leur
**progression** dans la mémorisation du Coran, puis on exporte les listes en **CSV**.

Projet **L2 Génie Logiciel** : la note porte sur la compréhension du code par chaque
membre, les fonctionnalités, l'UX/UI, le **respect strict de l'architecture MVC**, la
bonne **gestion des exceptions** et l'usage intelligent d'`Optional`.

## Stack & choix arrêtés (imposés par l'énoncé)

- **Java 21** (toolchain `maven-compiler-plugin`, `release=21`).
- **Java Swing uniquement** pour l'UI (aucune autre bibliothèque graphique).
- **Hibernate natif** (JPA / Jakarta Persistence) — `SessionFactory` singleton via
  `HibernateUtil` construit à partir de **`hibernate.cfg.xml`**. Aucun JDBC manuel pour le CRUD.
- **PostgreSQL** (le code métier ne dépend pas du SGBD : seul `hibernate.cfg.xml` change ;
  les blocs MySQL et Oracle sont fournis en commentaire).
- **Maven** (`pom.xml`) : `hibernate-core`, pilote `postgresql`, et `h2` + `junit-jupiter` en test.

## Architecture MVC imposée (à respecter EXACTEMENT)

```
sn.l2gl.aida.daara
├── model
│   ├── models      // entités JPA : Maitre, Classe, Talibe, Progression (+ enum Niveau)
│   └── dao         // Dao<T,ID> + GenericDao + MaitreDao, ClasseDao, TalibeDao, ProgressionDao
├── controller      // un contrôleur par entité
├── view            // un JPanel par entité + AppDaara (fenêtre + menu) + Theme
├── exception       // DaaraException + exceptions métier par entité
└── util            // HibernateUtil + CsvExporter
```

### Règles de couche (strictes)

- **models** : entités annotées JPA. Aucune logique d'interface.
- **dao** : accès aux données via Hibernate. **C'est ICI que se trouvent les requêtes HQL**
  et les recherches. Le DAO **lève** les exceptions métier.
- **controller** : reçoit la vue, branche les écouteurs (`addActionListener`), valide les
  saisies, appelle le DAO, **capture les exceptions** (try/catch) et affiche via `JOptionPane`,
  puis rafraîchit la vue. **Aucune requête SQL/HQL ici.**
- **view** : composants Swing exposés via *getters* + `afficher(List)`, `remplir(entité)`,
  `reinitialiser()`. **AUCUN accès BD, AUCUNE logique métier.** La vue **n'attrape jamais** d'exception.
- **AppDaara** : `JFrame` principale, menu « Affichage » + barre latérale (CardLayout).

### Recherches

- **Recherche unique** (par clé) → `Optional<T>` (`trouver`, `trouverObligatoire`).
- **Recherche par critère** (plusieurs résultats possibles) → `List<T>`
  (`rechercherParNom`, `rechercherParLibelle`, `listerParClasse`, `listerParTalibe`).

## Gestion des exceptions

Toutes héritent de `DaaraException` (`extends RuntimeException`) :

- `XIntrouvableException` — recherche unique sans résultat attendu.
- `XDejaExistant(e)Exception` — insertion d'une clé déjà existante.
- `ProgressionInvalideException` — `nombreVersets < 0`, sourate vide, talibé absent.
- `SuppressionImpossibleException` — relation existante (maître→classes, classe→talibés).

Règle : **le DAO lève**, **le contrôleur capture** et affiche, **la vue n'attrape jamais**.

## Règles métier (à ne pas casser)

1. **Maître** : matricule unique ; suppression interdite s'il encadre ≥ 1 classe.
2. **Classe** : code unique ; maître **obligatoire** (JComboBox) ; suppression interdite si ≥ 1 talibé.
3. **Talibé** : matricule unique ; classe **obligatoire** (JComboBox) ; sa suppression supprime ses progressions (cascade applicative dans `TalibeDao`).
4. **Progression** : talibé obligatoire (JComboBox) ; `nombreVersets ≥ 0` et sourate non vide ; filtrable par talibé.

Les champs maître/classe/talibé sont **toujours des listes déroulantes** alimentées depuis la base.

## Commandes utiles

```bash
# Définir le JDK (Windows / Git Bash)
export JAVA_HOME="/c/Program Files/Java/jdk-21.0.11"

# Compiler
./mvnw compile

# Lancer les tests (base H2 en mémoire — aucun PostgreSQL requis)
./mvnw test

# Lancer l'application Swing (nécessite PostgreSQL : base "daara")
./mvnw exec:java        # mainClass = sn.l2gl.aida.daara.view.AppDaara
```

### Base de données (PostgreSQL)

`hibernate.cfg.xml` pointe sur `jdbc:postgresql://localhost:5432/daara` (user `postgres`,
mot de passe `root`, `hbm2ddl.auto=update`). Créer la base au préalable :
`CREATE DATABASE daara;`. Pour changer de SGBD, ne modifier QUE `hibernate.cfg.xml`.

## Tests

- **JUnit 5** + base **H2 en mémoire** (`src/test/resources/hibernate.cfg.xml` masque la config Postgres).
- `DaaraTestBase` vide les tables avant chaque test (isolation).
- Couverts : CRUD générique, `modifier`, recherches par critère, et **toutes les règles métier**
  (doublons, suppressions interdites, cascade, progression invalide) + `CsvExporter`.

## Outillage `.claude/` du projet

- **agents/** : `java-reviewer`, `java-build-resolver`, `code-reviewer`, `security-reviewer`,
  `planner`, `architect`, `tdd-guide`, `refactor-cleaner`, `doc-updater`.
- **rules/** : `common/` + `java/` (coding-style, patterns, security, testing, hooks).
- **skills/** : `java-coding-standards`, `jpa-patterns`.
- **commands/** : `plan`, `code-review`, `quality-gate`, `test-coverage`, `refactor-clean`, `build-fix`.

## Conventions

- Commentaires en **français**, concis, axés sur le *pourquoi*.
- Un type public par fichier ; getters explicites (pas de Lombok).
- La charte graphique (couleurs, polices, composants stylisés) est centralisée dans `view/Theme.java`.
