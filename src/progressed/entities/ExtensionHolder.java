package progressed.entities;

/**
 * @author GlennFolker
 */
public interface ExtensionHolder{
    /** This will be called by {@link Extension#draw()} */
    void drawExt();

    /** @return The clipsize of the used {@link Extension} */
    float clipSizeExt();
}