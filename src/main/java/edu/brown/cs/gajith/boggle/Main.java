package edu.brown.cs.gajith.boggle;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

/** Entrypoint for invoking Boggle game.  If run with --generate,
 * a Boggle board will be printed to stdout.  If invoked with --solve
 * and a board, all possible words will be printed to stdout.
 * Otherwise, a UI will be made available at <a
 * href="http://localhost:4567/play">localhost:4567/play</a>.
 */

public abstract class Main {

  /** Method entrypoint for CLI invocation.
   *  @param args Arguments passed on the command line.
   */
  public static void main(String[] args) {
    OptionParser parser = new OptionParser();

    parser.accepts("generate");

    OptionSpec<String> solveSpec =
      parser.accepts("solve").withRequiredArg().ofType(String.class);

    OptionSpec<String> scoreSpec =
      parser.accepts("score").withRequiredArg().ofType(String.class);

    OptionSet options = parser.parse(args);

    if (options.has("generate")) {
      System.out.print(new Board().toString());
    } else if (options.has(solveSpec)) {
      Board provided = new Board(options.valueOf(solveSpec));
      for (String w : provided.play()) {
        System.out.printf("%s%n", w);
      }
    } else if (options.has(scoreSpec)) {
      Board provided = new Board(options.valueOf(scoreSpec));
    } else {
      runSparkServer();
      // runSparkServer() is NOT an infinite loop. Your program will get to
      // this line.  So why doesn't it exit?
    }
  }

  private static void runSparkServer() {
    // Have Spark only listen on localhost, so that connections from
    // other machines are ignored.
    String localhost = "127.0.0.1";
    Spark.ipAddress(localhost);

    // Spark listens on port 4567 by default.
    // Spark.port(4567);

    // We need to serve some simple static files containing CSS and JavaScript.
    // This tells Spark where to look for urls of the form "/static/*".
    Spark.externalStaticFileLocation("src/main/resources/static");

    // Development is easier if we show exceptions in the browser.
    Spark.exception(Exception.class, new ExceptionPrinter());

    // We'll render our responses with the FreeMaker template system.
    FreeMarkerEngine freeMarker = createEngine();

    // Setup two urls, one to play a new board, and one to show game results.
    Spark.get("/play", new PlayHandler(), freeMarker);
    Spark.post("/results", new ResultsHandler(), freeMarker);
  }

  /** A handler to produce a board to play.
   *  @return The title, a new board and the template to render them
   *  (play.ftl).
   */
  private static class PlayHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Board board = new Board();
      Map<String, Object> variables =
        ImmutableMap.of("title", "Boggle: Play a board",
                        "board", board);
      return new ModelAndView(variables, "play.ftl");
    }
  }

  /** A handler to produce the results of playing a game of Boggle.
   *  @return The title, board, and various results and the template
   *  to render them (results.ftl).
   */
  private static class ResultsHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();

      Board board = new Board(qm.value("board"));
      Set<String> legal = board.play();

      Iterable<String> guesses =
        BREAKWORDS.split(qm.value("guesses").toLowerCase());

      // We prefer to specify the more general interface types when
      // declaring variables, even though we must instantiate a more
      // specific, concrete type (TreeSet).
      // We also prefer to elide types by using the  "diamond operator". Why?
      SortedSet<String> good = new TreeSet<>();
      SortedSet<String> bad = new TreeSet<>();
      int score = 0;
      for (String word : guesses) {
        if (legal.contains(word)) {
          score += Board.score(word);
          good.add(word);
        } else {
          bad.add(word);
        }
      }

      // We know that 'missed' will also give results in sorted order,
      // based on Guava's Sets.difference spec:
      // http://guava-libraries.googlecode.com/svn/tags/release04/javadoc/com/google/common/collect/Sets.html#difference(java.util.Set, java.util.Set)
      // What does that documentation mean by a "view"?
      // Why don't we declare 'missed' as a SortedSet?
      Set<String> missed = Sets.difference(legal, good);

      // We can't use ImmutableMap.of(), as in PlayHandler. Why not?
      // This is the "Builder" pattern which is a nice way to create
      // complicated immutable objects.
      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
        .put("title", "Boggle: Results")
        .put("board", board)
        .put("score", score)
        .put("good", good)
        .put("bad", bad)
        .put("missed", missed).build();
      return new ModelAndView(variables, "results.ftl");
    }

    private static final Splitter BREAKWORDS =
      Splitter.on(Pattern.compile("\\W+")).omitEmptyStrings();
  }

  // Below here, there isn't much for a new CS32 student to worry
  // about understanding.

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.\n",
                        templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private static final int INTERNAL_SERVER_ERROR = 500;
  /** A handler to print an Exception as text into the Response.
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(INTERNAL_SERVER_ERROR);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

}
