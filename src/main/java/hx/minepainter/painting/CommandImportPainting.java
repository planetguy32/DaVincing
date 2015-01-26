package hx.minepainter.painting;

import java.util.concurrent.LinkedBlockingQueue;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CommandImportPainting extends CommandBase {

  private static final LinkedBlockingQueue<Runnable> tasks = new LinkedBlockingQueue();
  private static Thread worker;

  @Override
  public String getCommandName() {
    return "mpimport";
  }

  @Override
  public String getCommandUsage(ICommandSender var1) {
    return "mpimport <image url> [--size <w> <h>]\nto import image as w * h pieces of 16x16 paintings";
  }

  @Override
  public void processCommand(ICommandSender var1, String[] var2) {
    startWorking();
    int w = 1;
    int h = 1;
    String url = var2[0];
    for (int i = 0; i < var2.length; i++) {
      if ((var2[i].equals("--size")) && (var2.length - i > 2)) {
        w = Integer.parseInt(var2[(i + 1)]);
        h = Integer.parseInt(var2[(i + 2)]);
      }
    }
    // TODO: seems like decompile failed here
//    try {
//      img = ImageIO.read(new URL(url)); 
//    } catch (MalformedURLException e) {
//      BufferedImage img;
//      e.printStackTrace();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
  }

  private void startWorking() {
    if ((worker == null) || (!worker.isAlive())) {
      worker = new Thread() {
        @Override
        public void run() {
          try {
            for (;;) {
              ((Runnable) CommandImportPainting.tasks.take()).run();
            }
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      };
      worker.start();
    }
  }
}
