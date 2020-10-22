// (c) Facebook, Inc. and its affiliates. Confidential and proprietary.

import UIKit

final class PickerTableViewCell: UITableViewCell {

    // MARK: - Properties

    var dataSource: PickerCellViewModelAPI? {
        didSet {
            guard let dataSource = dataSource else { return }
            pickerView.selectRow(dataSource.selectedRow, inComponent: 0, animated: false)
            titleLabel.text = dataSource.title
            textField.text = dataSource.name(by: dataSource.selectedRow)
        }
    }

    private let pickerView = UIPickerView()
    @IBOutlet private var titleLabel: UILabel!
    @IBOutlet private var textField: UITextField!

    // MARK: - Lifecycle

    override func awakeFromNib() {
        super.awakeFromNib()

        pickerView.dataSource = self
        pickerView.delegate = self

        textField.inputView = pickerView
        textField.inputAccessoryView = createToolbar()
    }

    override func prepareForReuse() {
        super.prepareForReuse()
        dataSource = nil
    }

    private func createToolbar() -> UIToolbar {
        let toolbar = UIToolbar(frame: CGRect(x: 0, y: 0, width: UIScreen.main.bounds.width, height: 44))
        let flexItem = UIBarButtonItem(barButtonSystemItem: .flexibleSpace, target: nil, action: nil)
        let doneItem = UIBarButtonItem(barButtonSystemItem: .done, target: self, action: #selector(doneButtonTapped))
        toolbar.items = [flexItem, doneItem]
        toolbar.sizeToFit()
        return toolbar
    }

    // MARK: - Actions

    @objc
    private func doneButtonTapped() {
        textField.resignFirstResponder()
    }
}

// MARK: - UIPickerViewDelegate, UIPickerViewDataSource

extension PickerTableViewCell: UIPickerViewDelegate, UIPickerViewDataSource {
    func numberOfComponents(in pickerView: UIPickerView) -> Int { 1 }

    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        dataSource?.numberOfRows ?? 0
    }

    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        dataSource?.name(by: row)
    }

    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        dataSource?.select(index: row)
        textField.text = dataSource?.name(by: row)
    }
}
