// (c) Facebook, Inc. and its affiliates. Confidential and proprietary.

import UIKit
import FBAudienceNetwork

final class SettingScreenViewController: UITableViewController {

    // MARK: - Properties

    private lazy var adTypeCellViewModel = AdTypeCellViewModel()
    private lazy var logLevelCellViewModel = LogLevelCellViewModel()

    // MARK: - Lifecylce

    override func viewDidLoad() {
        super.viewDidLoad()
        navigationItem.title = "Settings"
        tableView.register(
            UINib(nibName: "TestModeTableViewCell", bundle: nil),
            forCellReuseIdentifier: "TestModeTableViewCell"
        )
        tableView.register(
            UINib(nibName: "PickerTableViewCell", bundle: nil),
            forCellReuseIdentifier: "PickerTableViewCell"
        )
        tableView.rowHeight = 48
        tableView.allowsSelection = false
    }

    // MARK: - Table View

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int { 3 }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        switch indexPath.row {
        case 0:
            let cell = tableView.dequeueReusableCell(withIdentifier: "TestModeTableViewCell", for: indexPath) as! TestModeTableViewCell
            return cell
        case 1:
            let cell = tableView.dequeueReusableCell(withIdentifier: "PickerTableViewCell", for: indexPath) as! PickerTableViewCell
            cell.dataSource = adTypeCellViewModel
            return cell
        case 2:
            let cell = tableView.dequeueReusableCell(withIdentifier: "PickerTableViewCell", for: indexPath) as! PickerTableViewCell
            cell.dataSource = logLevelCellViewModel
            return cell
        default:
            return UITableViewCell()
        }
    }

    override func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        "SDK Version \(FB_AD_SDK_VERSION)"
    }

    // MARK: - Actions

    @IBAction private func dismissButtonTapped() {
        dismiss(animated: true, completion: nil)
    }
}
