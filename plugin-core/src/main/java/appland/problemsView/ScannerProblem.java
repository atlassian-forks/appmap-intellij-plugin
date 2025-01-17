package appland.problemsView;

import appland.Icons;
import appland.problemsView.model.ScannerFinding;
import com.intellij.analysis.problemsView.FileProblem;
import com.intellij.analysis.problemsView.ProblemsProvider;
import com.intellij.openapi.vfs.VirtualFile;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public final class ScannerProblem implements FileProblem {
    private final @NotNull ProblemsProvider provider;
    private final @NotNull VirtualFile annotatedFile;
    @Getter
    private final @NotNull ScannerFinding finding;
    // 0-based
    private final int line;

    public ScannerProblem(@NotNull ProblemsProvider provider,
                          @NotNull VirtualFile annotatedFile,
                          @NotNull ScannerFinding finding) {
        this.provider = provider;
        this.annotatedFile = annotatedFile;
        this.finding = finding;

        var location = finding.getProblemLocation();
        this.line = location != null ? location.getZeroBasedLine(-1) : -1;
    }

    public @Nullable VirtualFile getFindingsFile() {
        return finding.getFindingsFile();
    }

    /**
     * @return The annotated source file, e.g. a Java file.
     */
    @NotNull
    @Override
    public VirtualFile getFile() {
        return annotatedFile;
    }

    @Override
    public int getLine() {
        return line;
    }

    @Override
    public int getColumn() {
        return -1;
    }

    @NotNull
    @Override
    public String getText() {
        return finding.getFindingTitle();
    }

    @Nullable
    @Override
    public String getDescription() {
        return finding.getDescription();
    }

    @Nullable
    @Override
    public String getGroup() {
        return null;
    }

    @NotNull
    @Override
    public Icon getIcon() {
        return Icons.APPMAP_FILE_SMALL;
    }

    @NotNull
    @Override
    public ProblemsProvider getProvider() {
        return provider;
    }
}
