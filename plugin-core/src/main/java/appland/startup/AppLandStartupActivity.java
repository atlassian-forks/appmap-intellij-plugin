package appland.startup;

import appland.installGuide.InstallGuideEditorProvider;
import appland.installGuide.InstallGuideViewPage;
import appland.problemsView.FindingsManager;
import appland.settings.AppMapApplicationSettingsService;
import appland.telemetry.TelemetryService;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.startup.StartupManager;
import org.jetbrains.annotations.NotNull;

public class AppLandStartupActivity implements StartupActivity {
    @Override
    public void runActivity(@NotNull Project project) {
        // load initial findings of the project
        FindingsManager.getInstance(project).reloadAsync();

        // show AppMap intro at first start
        var unitTestMode = ApplicationManager.getApplication().isUnitTestMode();
        if (AppMapApplicationSettingsService.getInstance().isFirstStart() && !unitTestMode) {
            AppMapApplicationSettingsService.getInstance().setFirstStart(false);

            openToolWindowAndQuickstart(project);

            var telemetry = TelemetryService.getInstance();
            telemetry.notifyTelemetryUsage(project);
            telemetry.sendEvent("plugin:install");
        }
    }

    static void openToolWindowAndQuickstart(@NotNull Project project) {
        StartupManager.getInstance(project).runWhenProjectIsInitialized(() -> {
            InstallGuideEditorProvider.open(project, InstallGuideViewPage.InstallAgent);
        });
    }
}