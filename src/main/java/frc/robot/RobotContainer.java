// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.commands.AutonomousThingy;
import frc.robot.commands.Drive;
import frc.robot.commands.ExampleCommand;
import frc.robot.commands.MoveDoor;
import frc.robot.subsystems.Door;
import frc.robot.commands.ShootCommand;

import frc.robot.commands.SwapDriveMode;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final Door m_door = new Door();
  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  private final Joystick j_joystick = new Joystick(Constants.Controls.JOYSTICK_USB);
  private final DriveTrain m_driveTrain = new DriveTrain(true); //parameter defines whether driving with CAN speed controllers or PWM
  private final AutonomousThingy a_autononousThingy = new AutonomousThingy(m_driveTrain);
  private final JoystickButton b_doorButton = new JoystickButton(j_joystick, 3);
  private final JoystickButton b_doorButton_2 = new JoystickButton(j_joystick, 4);


  private final JoystickButton b_enableFlyWheel = new JoystickButton(j_joystick, Constants.Controls.BUTTON_SHOOT_FLYWHEEL);
  private final Shooter flyWheel = new Shooter();
  private final JoystickButton b_resetNAVX;
  private final JoystickButton b_swapDir;
  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    b_swapDir = new JoystickButton(j_joystick, Constants.Controls.BUTTON_SWAP_DRIVE_DIR);
    b_resetNAVX = new JoystickButton(j_joystick, Constants.Controls.BUTTON_RESET_NAVX);
    // Configure the button bindings
    configureButtonBindings();
    CameraServer.startAutomaticCapture();
   // CvSink cvSink = CameraServer.getVideo();
   // CvSource outputStream = CameraServer.putVideo("Name", 160, 120);
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    b_resetNAVX.whileHeld(new RunCommand(() -> m_driveTrain.NAVX.zeroYaw(),m_driveTrain));
    m_driveTrain.setDefaultCommand(new Drive(m_driveTrain, j_joystick));
    b_doorButton.whileHeld(new MoveDoor(m_door, true));
    b_doorButton_2.whileHeld(new MoveDoor(m_door, false));
    b_enableFlyWheel.whileHeld(new ShootCommand(flyWheel, 1.0 ));

    b_swapDir.whileHeld(new SwapDriveMode(m_driveTrain));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An AutonomousThingy will run in autonomous
    return a_autononousThingy;
  }
}
