package dev.hypnotic.ui;

import dev.hypnotic.module.options.OptionModule;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import dev.hypnotic.ui.clickgui.ModuleButton;
import dev.hypnotic.ui.clickgui.settings.SettingsWindow;
import dev.hypnotic.ui.clickgui2.MenuBar;
import net.minecraft.client.util.math.MatrixStack;

public class OptionsScreen extends HypnoticScreen {

	public static OptionsScreen INSTANCE = new OptionsScreen();
	private MenuBar menuBar;
	public Frame frame;
	private SettingsWindow window;
	
	// Options
	public BooleanSetting forceCFont = new BooleanSetting("Force Custom Font In Guis", false);
	public BooleanSetting disableGradient = new BooleanSetting("Disable Gradient In Guis", false);
	
	// Baritone settings
	public BooleanSetting allowParkour = new BooleanSetting("Baritone Allow Parkour", true);
	public BooleanSetting allowBreak = new BooleanSetting("Baritone Allow Break", true);
	public BooleanSetting allowSprint = new BooleanSetting("Baritone Allow Sprint", true);
	public BooleanSetting allowPlace = new BooleanSetting("Baritone Allow Place", true);
	public BooleanSetting allowInventory = new BooleanSetting("Baritone Allow Inventory", false);
	public BooleanSetting chatControl = new BooleanSetting("Baritone Allow Chat Control", false);
	public BooleanSetting assumeJesus = new BooleanSetting("Baritone Assume Jesus", false);
	public BooleanSetting assumeSafewalk = new BooleanSetting("Baritone Assume Safewalk", false);
	public BooleanSetting assumeStep = new BooleanSetting("Baritone Assume Step", false);
	
	public OptionModule options;
	
	public OptionsScreen() {
		this.frame = new Frame("Options", 100, 100, 100, 100);
		options = new OptionModule(forceCFont, disableGradient, allowParkour, allowBreak, allowSprint, allowPlace, allowInventory, chatControl, assumeJesus, assumeSafewalk, assumeStep);
		window = new SettingsWindow(new ModuleButton(options, null, 0, 0));
	}
	
	@Override
	protected void init() {
		menuBar = MenuBar.INSTANCE;
		super.init();
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		menuBar.renderMenuBar(matrices, mouseX, mouseY, width, height);
		window.render(matrices, mouseX, mouseY, delta);
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		menuBar.mouseClicked((int)mouseX, (int)mouseY, button);
		window.mouseClicked(mouseX, mouseY, button);
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		window.mouseReleased(mouseX, mouseY, button);
		return super.mouseReleased(mouseX, mouseY, button);
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		window.mouseScrolled(amount);
		return super.mouseScrolled(mouseX, mouseY, amount);
	}
	
	@Override
	public boolean isPauseScreen() {
		return false;
	}
	
//	public static class Button {
//		public String name;
//		public Setting setting;
//		public float x, y, width, height;
//		
//		public Button(String name, Setting setting, float x, float y, float width, float height) {
//			this.name = name;
//			this.setting = setting;
//			this.x = x;
//			this.y = y;
//			this.width = width;
//			this.height = height;
//		}
//		
//		public void render(MatrixStack matrices, int mouseX, int mousey, float partialTicks) {
//			
//		}
//		
//		public void mouseClicked(double mouseX, double mouseY, int button) {
//			
//		}
//	}
}
